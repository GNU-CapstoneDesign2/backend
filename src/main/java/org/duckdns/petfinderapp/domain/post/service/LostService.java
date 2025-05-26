package org.duckdns.petfinderapp.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.post.dto.request.*;
import org.duckdns.petfinderapp.domain.post.dto.response.ResLost;
import org.duckdns.petfinderapp.domain.post.entity.Image;
import org.duckdns.petfinderapp.domain.post.entity.Lost;
import org.duckdns.petfinderapp.domain.post.repository.LostRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostService {
    private final LostRepository lostRepository;
    private final FileService fileService;

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
                String filePath = fileService.saveFile(file);
                Image img = Image.builder()
                        .origFileName(file.getOriginalFilename())
                        .filePath(filePath)
                        .fileSize(file.getSize())
                        .build();
                lost.addImage(img);
            }
        }

        return lostRepository.save(lost).getId();
    }

    // Lost 조회
    @Transactional(readOnly = true)
    public ResLost searchLost(Long id) {
        Lost lost = lostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        return new ResLost(lost);
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
            lost.getImages().clear();

            List<Image> img = image.stream()
                    .map(file -> {
                        String imageUrl = fileService.upload(file);
                        return Image.builder()
                                .origFileName(file.getOriginalFilename())
                                .filePath(imageUrl)
                                .fileSize(file.getSize())
                                .build();
                    })
                    .collect(Collectors.toList());

            lost.updateImage(img);
        }

        return id;
    }
}
