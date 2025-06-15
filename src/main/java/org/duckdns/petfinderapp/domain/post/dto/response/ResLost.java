package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Lost;

@Getter
public class ResLost {
    private Long id;
    private ResCommon common;
    private LostDto lost;

    public ResLost(Lost lost) {
        this.id = lost.getId();
        this.common = ResCommon.of(lost);
        this.lost = new LostDto(lost);
    }

    public ResLost(Lost lost, Long chatRoomId) {
        this.id = lost.getId();
        this.common = ResCommon.of(lost, chatRoomId);
        this.lost = new LostDto(lost);
    }
}
