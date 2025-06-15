package org.duckdns.petfinderapp.domain.post.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.post.dto.request.CommonCreate;
import org.duckdns.petfinderapp.domain.post.dto.request.CommonUpdate;
import org.duckdns.petfinderapp.domain.post.dto.response.PostSummaryResponse;
import org.duckdns.petfinderapp.domain.post.dto.response.ResCommon;
import org.duckdns.petfinderapp.domain.post.entity.Adopt;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.entity.Found;
import org.duckdns.petfinderapp.domain.post.entity.Image;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.repository.AdoptRepository;
import org.duckdns.petfinderapp.domain.post.repository.PostRepository;
import org.duckdns.petfinderapp.domain.similarity.dto.request.ImageAiEmbeddingRequest;
import org.duckdns.petfinderapp.domain.similarity.service.EmbeddingService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final AdoptRepository adoptRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final S3Uploader s3Uploader;
  private final EmbeddingService embeddingService;

  // 게시글 작성
  @Transactional
  public Long create(CommonCreate commonCreate, List<MultipartFile> image, User user) {
    Found common = commonCreate.toFound(user);

    if (image != null && !image.isEmpty()) {
      for (MultipartFile file : image) {
        try {
          // S3에 업로드
          String imageUrl = s3Uploader.upload(file, "images");

          // 이미지 Entity
          Image img = Image.builder()
              .origFileName(file.getOriginalFilename())
              .fileURL(imageUrl)  // S3 URL 저장
              .fileSize(file.getSize())
              .build();

          // 게시글에 이미지 추가
          common.addImage(img);

        } catch (IOException e) {
          throw new RuntimeException("이미지 업로드 실패: " + file.getOriginalFilename(), e);
        }
      }
    }

    PostCommon savedPost = postRepository.save(common);

    // 이미지 임베딩 요청 이벤트 발행
    embeddingService.sendOtherEmbeddingRequest(ImageAiEmbeddingRequest.of(savedPost));

    return savedPost.getId();
  }

  // Found 조회
  @Transactional(readOnly = true)
  public ResCommon searchFound(Long id, User user) {
    PostCommon common = postRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
    if (!(common instanceof Found found)) {
      throw new IllegalArgumentException("해당 게시물은 FOUND 타입이 아닙니다.");
    }

    // 게시글 작성자인 경우 chatRoomId는 null
    if (found.getUser().getId().equals(user.getId())) {
      return ResCommon.of(found);
    }

    // 사용자가 참여 중인 채팅방 조회
    Long chatRoomId = chatRoomRepository.findByPostIdAndUserId(id, user.getId())
            .map(ChatRoom::getId)
            .orElse(null);

    return ResCommon.of(found, chatRoomId);
  }


  // found 게시글 수정
  @Transactional
  public Long update(Long id, CommonUpdate dto, List<MultipartFile> image, User user) {
    PostCommon common = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 게시물이 존재하지 않습니다."));
    if (!(common instanceof Found found)) {
      throw new RuntimeException("해당 게시물은 FOUND 타입이 아닙니다.");
    }

    // 게시글 작성자와 현재 로그인한 사용자가 일치하는지 확인
    if (!found.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("게시글 작성자만 수정할 수 있습니다.");
    }
    Coordinates updateCoordinates = dto.getCoordinates().toEntity();

    found.update(
        dto.getAddress(),
        dto.getPetType(),
        dto.getContent(),
        updateCoordinates
    );

    if (image != null && !image.isEmpty()) {
      // 기존 이미지 S3에서 삭제
      List<Image> existingImages = new ArrayList<>(common.getImages());
      for (Image img : existingImages) {
        s3Uploader.delete(img.getFileURL());
      }

      // 새 이미지 목록 생성
      List<Image> newImages = new ArrayList<>();
      for (MultipartFile file : image) {
        try {
          String imageUrl = s3Uploader.upload(file, "images");

          Image img = Image.builder()
              .origFileName(file.getOriginalFilename())
              .fileURL(imageUrl)
              .fileSize(file.getSize())
              .build();

          newImages.add(img);
        } catch (IOException e) {
          throw new RuntimeException("이미지 업로드 실패: " + file.getOriginalFilename(), e);
        }
      }

      common.updateImage(newImages);
    }

    return id;
  }

  // 게시글 삭제
  @Transactional
  public void delete(Long id, User user) {
    PostCommon common = postRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

    // 게시글 작성자와 현재 로그인한 사용자가 일치하는지 확인
    if (!common.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("게시글 작성자만 삭제할 수 있습니다.");
    }

    postRepository.delete(common);
  }

  @Transactional
  public List<Adopt> upsertAdoptsAndReturnAdoptNeedsEmbedding(List<Adopt> newAdoptList) {
    List<String> newDesertionNumList = newAdoptList.stream()
        .map(Adopt::getDesertionNum)
        .toList();

    List<Adopt> existingAdopts = adoptRepository.findAllByDesertionNumIn(newDesertionNumList);

    Map<String, Adopt> existingMap = existingAdopts.stream()
        .collect(Collectors.toMap(Adopt::getDesertionNum, Function.identity()));

    List<Adopt> newAdopts = new ArrayList<>();
    List<Adopt> needsEmbedding = new ArrayList<>();

    for (Adopt newAdopt : newAdoptList) {
      Adopt existingAdopt = existingMap.get(newAdopt.getDesertionNum());
      if (existingAdopt != null) {
        if (hasImageChanged(existingAdopt, newAdopt)) {
          needsEmbedding.add(existingAdopt);
        }

        existingAdopt.updateWith(newAdopt);

      } else {
        newAdopts.add(newAdopt);
      }
    }

    List<Adopt> savedNew = adoptRepository.saveAll(newAdopts);
    needsEmbedding.addAll(savedNew);
    return needsEmbedding;
  }

  @Transactional(readOnly = true)
  public Page<PostSummaryResponse> getMyPosts(User user, Pageable pageable) {
    return postRepository.findAllByUser(user, pageable).map(PostSummaryResponse::of);
  }

  private boolean hasImageChanged(Adopt existing, Adopt updated) {
    if (existing.getImages().size() != updated.getImages().size()) {
      return true;
    }
    for (int i = 0; i < existing.getImages().size(); i++) {
      Image existingImage = existing.getImages().get(i);
      Image updatedImage = updated.getImages().get(i);
      if (!existingImage.getFileURL().equals(updatedImage.getFileURL())) {
        return true;
      }
    }
    return false;
  }
}
