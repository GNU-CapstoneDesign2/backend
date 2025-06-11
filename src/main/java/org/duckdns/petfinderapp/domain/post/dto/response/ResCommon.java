package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResCommon {
    private Long id;
    private Long userId;
    private PostState state;
    private LocalDateTime createdAt;
    private LocalDateTime date;
    private String address;
    private PetType petType;
    private String content;
    private ResCoordinates coordinates;
    private List<ImageDto> images;

    public static ResCommon of(PostCommon post) {
        return ResCommon.builder()
                .id(post.getId())
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .state(post.getState())
                .createdAt(post.getCreateAt())
                .date(post.getDate())
                .address(post.getAddress())
                .petType(post.getPetType())
                .content(post.getContent())
                .coordinates(new ResCoordinates(post.getCoordinates()))
                .images(post.getImages().stream().map(ImageDto::new).collect(Collectors.toList()))
                .build();
    }
}
