package org.duckdns.petfinderapp.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.entity.Found;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommonCreate {
    private PostState state;
    private LocalDateTime date;
    private String address;
    private PetType petType;
    private String content;
    private ReqCoordinates coordinates;

    public PostCommon toEntity() {
        Coordinates coordEntity = coordinates.toEntity();

        return PostCommon.builder()
                .state(state)
                .date(date)
                .address(address)
                .petType(petType)
                .content(content)
                .coordinates(coordEntity)
                .build();
    }

    public Found toFound(User user) {
        Coordinates coordEntity = coordinates.toEntity();

        return Found.builder()
                .user(user)
                .state(state)
                .date(date)
                .address(address)
                .petType(petType)
                .content(content)
                .coordinates(coordEntity)
                .build();
    }
}
