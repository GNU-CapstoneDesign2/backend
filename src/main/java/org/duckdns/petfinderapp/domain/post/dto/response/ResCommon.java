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

    public static ResCommon of(PostCommon Entity) {
        return ResCommon.builder()
                .id(Entity.getId())
                .userId(Entity.getUser() != null ? Entity.getUser().getId() : null)
                .state(Entity.getState())
                .createdAt(Entity.getCreateAt())
                .date(Entity.getDate())
                .address(Entity.getAddress())
                .petType(Entity.getPetType())
                .content(Entity.getContent())
                .coordinates(new ResCoordinates(Entity.getCoordinates()))
                .images(Entity.getImages().stream().map(ImageDto::new).collect(Collectors.toList()))
                .build();
    }
}
