package org.duckdns.petfinderapp.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.post.dto.request.CommonCreate;
import org.duckdns.petfinderapp.domain.post.dto.request.CommonUpdate;
import org.duckdns.petfinderapp.domain.post.dto.response.ResCommon;
import org.duckdns.petfinderapp.domain.post.entity.*;
import org.duckdns.petfinderapp.domain.post.repository.PostRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FileService fileService;

    // 게시글 작성
    @Transactional
    public Long create(CommonCreate commonCreate, List<MultipartFile> image, User user) {
        Found common = commonCreate.toFound(user);

        if(image != null && !image.isEmpty()) {
            for(MultipartFile file : image) {
                String filePath = fileService.saveFile(file);
                Image img  = Image.builder()
                        .origFileName(file.getOriginalFilename())
                        .filePath(filePath)
                        .fileSize(file.getSize())
                        .build();

                common.addImage(img);
            }
        }

        return postRepository.save(common).getId();
    }

    // Found 조회
    @Transactional(readOnly = true)
    public ResCommon searchFound(Long id){
        PostCommon common = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        if (!(common instanceof Found found)) {
            throw new IllegalArgumentException("해당 게시물은 FOUND 타입이 아닙니다.");
        }
        return ResCommon.of(found);
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
            List<Image> img = image.stream()
                    .map(file -> {
                        String imageUrl = fileService.upload(file);
                        return Image.builder()
                                .origFileName(file.getOriginalFilename())
                                .filePath(imageUrl)
                                .fileSize(file.getSize())
                                .common(found)
                                .build();
                    })
                    .collect(Collectors.toList());

            found.updateImage(img);
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
}
