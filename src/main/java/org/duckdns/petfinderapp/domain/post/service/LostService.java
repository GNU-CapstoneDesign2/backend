package org.duckdns.petfinderapp.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.post.dto.request.*;
import org.duckdns.petfinderapp.domain.post.dto.response.ResLost;
import org.duckdns.petfinderapp.domain.post.entity.Image;
import org.duckdns.petfinderapp.domain.post.entity.Lost;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.repository.LostRepository;
import org.duckdns.petfinderapp.domain.similarity.dto.request.ImageAiEmbeddingRequest;
import org.duckdns.petfinderapp.domain.similarity.service.EmbeddingService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LostService {
    private final LostRepository lostRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final S3Uploader s3Uploader;
    private final EmbeddingService embeddingService;

    // 게시글 작성
    @Transactional
    public Long create(LostCreate dto, List<MultipartFile> image, User user) {
        CommonCreate commonDto = dto.getCommonCreate();
        ReqLost req = dto.getReqLost();

        Lost lost = Lost.builder()
                // 부모 필드
                .user(user)
                .date(commonDto.getDate())
                .address(commonDto.getAddress())
                .coordinates(commonDto.getCoordinates().toEntity())
                .petType(commonDto.getPetType())
                .content(commonDto.getContent())
                .state(commonDto.getState())
                // 자식 필드
                .name(req.getName())
                .gender(req.getGender())
                .neuter(req.getNeuter())
                .petNum(req.getPetNum())
                .breed(req.getBreed())
                .phone(req.getPhone())
                .reward(req.getReward())
                .build();

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
                    lost.addImage(img);

                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 실패: " + file.getOriginalFilename(), e);
                }
            }
        }

        PostCommon savedPost = lostRepository.save(lost);

        // 이미지 임베딩 요청 이벤트 발행
        embeddingService.sendLostEmbeddingRequest(ImageAiEmbeddingRequest.of(savedPost));

        return savedPost.getId();
    }

    // Lost 조회
    @Transactional(readOnly = true)
    public ResLost searchLost(Long id, User user) {
        Lost lost = lostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        // 게시글 작성자인 경우 chatRoomId는 null
        if (lost.getUser().getId().equals(user.getId())) {
            return new ResLost(lost);
        }

        // 사용자가 참여 중인 채팅방 조회
        Long chatRoomId = chatRoomRepository.findByPostIdAndUserId(id, user.getId())
                .map(ChatRoom::getId)
                .orElse(null);

        return new ResLost(lost, chatRoomId);
    }

    // lost 게시글 수정
    @Transactional
    public Long update(Long id, LostUpdate dto, List<MultipartFile> image, User user) {
        Lost lost = lostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시물이 존재하지 않습니다."));

        if (!lost.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("해당 게시물은 수정할 권한이 없습니다.");
        }

        CommonUpdate commonUpdate = dto.getCommonUpdate();
        ReqLost reqLost = dto.getReqLost();

        lost.update(
                commonUpdate.getAddress(),
                commonUpdate.getPetType(),
                commonUpdate.getContent(),
                commonUpdate.getCoordinates().toEntity()
        );

        lost.update(
                reqLost.getName(),
                reqLost.getGender(),
                reqLost.getPetNum(),
                reqLost.getBreed(),
                reqLost.getPhone(),
                reqLost.getReward()
        );


        if (image != null && !image.isEmpty()) {
            // 기존 이미지 S3에서 삭제
            List<Image> existingImages = new ArrayList<>(lost.getImages());
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

            lost.updateImage(newImages);
        }

        return id;
    }
}
