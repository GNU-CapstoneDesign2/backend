package org.duckdns.petfinderapp.domain.map.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PostState;

@Builder
public record MapPostsResponse(
    String postId,
    PostState state,
    LocalDateTime date,
    String address,
    String description,
    String imageUrl
) {
  public static MapPostsResponse of(PostCommon postCommon, String thumbnailImageUrl) {
    return MapPostsResponse.builder()
        .postId(postCommon.getId().toString())
        .state(postCommon.getState())
        .date(postCommon.getDate())
        .address(postCommon.getAddress())
        .description(postCommon.getContent())
        .imageUrl(thumbnailImageUrl)
        .build();
  }
}
