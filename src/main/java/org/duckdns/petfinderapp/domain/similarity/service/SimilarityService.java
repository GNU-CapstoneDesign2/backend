package org.duckdns.petfinderapp.domain.similarity.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.post.exception.PostNotFoundException;
import org.duckdns.petfinderapp.domain.post.repository.PostRepository;
import org.duckdns.petfinderapp.domain.push.dto.FcmResponseDto;
import org.duckdns.petfinderapp.domain.push.service.FcmService;
import org.duckdns.petfinderapp.domain.similarity.client.ImageAiClient;
import org.duckdns.petfinderapp.domain.similarity.dto.item.SseSimilarityItem;
import org.duckdns.petfinderapp.domain.similarity.dto.request.SimilarityRequest;
import org.duckdns.petfinderapp.domain.similarity.dto.response.ImageAiSimilarityResponse;
import org.duckdns.petfinderapp.domain.similarity.dto.response.SseSimilarityResponse;
import org.duckdns.petfinderapp.domain.similarity.entity.Similarity;
import org.duckdns.petfinderapp.domain.similarity.exception.SimilarityAccessDeniedException;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimilarityService {

  private final ImageAiClient imageAiClient;
  private final SimilarityRepository similarityRepository;
  private final FcmService fcmService;
  private final PostRepository postRepository;
  private final SimilarityNotifier notifier;

  // 실종 게시글 이미지 유사도 계산
  // 목격/공고/입양 게시글 이미지 유사도 계산
  // 임베딩 완료 시 유사도 연산
  @Transactional
  public void processSimilarity(Long postId, PostState postState) {
    if (postState == PostState.LOST) {
      processLostSimilarity(postId, postState);
    } else {
      processOtherSimilarity(postId, postState);
    }
  }

  private void processLostSimilarity(Long postId, PostState postState) {
    log.info("유사 게시글 조회 시작: postId={}, postState={}", postId, postState);
    PostCommon postCommon = postRepository.findById(postId)
        .orElseThrow(PostNotFoundException::missingPostCommon);

    SimilarityRequest similarityRequest = SimilarityRequest.of(postId, postState,
        getImageUrl(postCommon));
    ImageAiSimilarityResponse response = imageAiClient.fetchSimilarOtherPosts(similarityRequest);
    log.info("유사 목격/입양/공고 게시글 조회 완료: postId={}, response={}", postId, response);

    List<Similarity> savedSimilarityList = saveSimilarResponse(postState, response);

    notifier.notifyComplete(postId, SseSimilarityResponse.of(
        savedSimilarityList.stream()
            .map(SseSimilarityItem::of)
            .toList(),
        savedSimilarityList.size()
    ));
  }

  private String getImageUrl(PostCommon postCommon) {
    return postCommon.getImages().isEmpty() ? null : postCommon.getImages().get(0).getFileURL();
  }

  private void processOtherSimilarity(Long postId, PostState postState) {
    PostCommon postCommon = postRepository.findById(postId)
        .orElseThrow(PostNotFoundException::missingPostCommon);

    SimilarityRequest similarityRequest = SimilarityRequest.of(postId, postState,
        getImageUrl(postCommon));
    ImageAiSimilarityResponse response = imageAiClient.fetchSimilarLostPosts(similarityRequest);
    log.info("유사 실종 게시글 조회 완료: postId={}, response={}", postId, response);

    saveSimilarResponse(postState, response);

    response.postIdList().forEach(this::sendSimilarPostPushNotification);
  }

  private void sendSimilarPostPushNotification(Long postId) {
    PostCommon post = postRepository.findById(postId)
        .orElseThrow(PostNotFoundException::missingPostCommon);

		fcmService.sendMessage(
        post.getUser(),
        FcmResponseDto.of(
            null,
            "새로운 게시글",
            "등록된 게시글과 유사한 " + post.getState().toKoreanString() + "글이 올라왔어요",
            post.getFirstImageUrl()+","+post.getId().toString()
				)
    );
		log.info("유사 게시글 푸시 알림 전송: postId={}, user={}", postId, post.getUser().getId());
  }

  private List<Similarity> saveSimilarResponse(PostState postState,
      ImageAiSimilarityResponse response) {
    PostCommon post = postRepository.findById(response.postId())
        .orElseThrow(PostNotFoundException::missingPostCommon);
    List<PostCommon> similarPostList = postRepository.findAllById(response.postIdList());
    List<Similarity> similarityList = similarPostList.stream()
        .map(similarPost -> {
          if (postState == PostState.LOST) {
            return Similarity.builder()
                .lostPost(post)
                .similarPost(similarPost)
                .build();
          } else {
            return Similarity.builder()
                .lostPost(similarPost)
                .similarPost(post)
                .build();
          }
        })
        .toList();

    return similarityRepository.saveAll(similarityList);
  }

  @Transactional(readOnly = true)
  public SseEmitter subscribeToSimilarity(User user, Long postId) {
    PostCommon post = postRepository.findById(postId)
        .orElseThrow(PostNotFoundException::missingPostCommon);
    if (!post.getUser().getId().equals(user.getId())) {
      throw SimilarityAccessDeniedException.accessDenied();
    }
    if (post.getState() != PostState.LOST) {
      throw SimilarityAccessDeniedException.accessDenied();
    }

    SseEmitter emitter = notifier.register(postId);

    // 1) 기존 데이터 조회
    List<SseSimilarityItem> existing = similarityRepository.findAllWithImagesByLostPostId(postId)
        .stream().map(SseSimilarityItem::of).toList();

    // 2) 이미 값이 있으면 즉시 전송
    if (!existing.isEmpty()) {
      notifier.notifyComplete(postId, SseSimilarityResponse.of(existing, existing.size()));
    }

    // 3) 없으면 notifier에 등록
    return emitter;
  }
}
