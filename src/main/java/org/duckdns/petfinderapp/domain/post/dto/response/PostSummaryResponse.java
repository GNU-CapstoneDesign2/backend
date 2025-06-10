package org.duckdns.petfinderapp.domain.post.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PostState;

@Builder
public record PostSummaryResponse(
    String postId,
    PostState state,
    LocalDateTime date,
    String address,
    String description,
    String imageUrl
) {

  public static PostSummaryResponse of(PostCommon postCommon) {
    String imageUrl = postCommon.getImages().isEmpty() ?
        null : postCommon.getImages().get(0).getFileURL();

    return PostSummaryResponse.builder()
        .postId(postCommon.getId().toString())
        .state(postCommon.getState())
        .date(postCommon.getDate())
        .address(postCommon.getAddress())
        .description(postCommon.getContent())
        .imageUrl(imageUrl)
        .build();
  }
}
