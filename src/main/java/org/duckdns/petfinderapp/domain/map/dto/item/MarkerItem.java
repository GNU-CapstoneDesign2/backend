package org.duckdns.petfinderapp.domain.map.dto.item;

import lombok.Builder;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;

@Builder
public record MarkerItem (
    Long postId,
    String address,
    PostState state,
    PetType species,
    Coordinates coordinate,
    String imageUrl
){

  public static MarkerItem of(PostCommon postCommon, String fileURL) {
    return MarkerItem.builder()
        .postId(postCommon.getId())
        .address(postCommon.getAddress())
        .state(postCommon.getState())
        .species(postCommon.getPetType())
        .coordinate(postCommon.getCoordinates())
        .imageUrl(fileURL)
        .build();
  }
}
