package org.duckdns.petfinderapp.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.entity.Found;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommonCreate {
    private Long userId;
    private PostState state;
    private LocalDateTime date;
    private String address;
    private PetType petType;
    private String content;
    private ReqCoordinates coordinates;

    public PostCommon toEntity() {
        Coordinates coordEntity = coordinates.toEntity();

        return PostCommon.builder()
                .id(userId)
                .state(state)
                .date(date)
                .address(address)
                .petType(petType)
                .content(content)
                .coordinates(coordEntity)
                .build();
    }

    public Found toFound() {
        Coordinates coordEntity = coordinates.toEntity();

        return Found.builder()
                .id(userId)
                .state(state)
                .date(date)
                .address(address)
                .petType(petType)
                .content(content)
                .coordinates(coordEntity)
                .build();
    }
}
