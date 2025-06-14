package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResCommon {
    private Long id;
    private Long userId;
    private String userName;
    private String userImg;
    private PostState state;
    private LocalDateTime createdAt;
    private LocalDateTime date;
    private String address;
    private PetType petType;
    private String content;
    private ResCoordinates coordinates;
    private List<ImageDto> images;

    public static ResCommon of(PostCommon post) {
        User user = post.getUser();

        return ResCommon.builder()
                .id(post.getId())
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .userName(user != null ? user.getName() : null)
                .userImg(user != null ? user.getImageUrl() : null)
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
