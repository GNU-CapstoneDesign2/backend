package org.duckdns.petfinderapp.domain.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PetType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonUpdate {
    private String address;
    private PetType petType;
    private String content;
    private ReqCoordinates coordinates;

    public PostCommon toEntity(){
        Coordinates coordEntity = coordinates.toEntity();

        return PostCommon.builder()
                .address(address)
                .petType(petType)
                .content(content)
                .coordinates(coordEntity)
                .build();
    }
}
