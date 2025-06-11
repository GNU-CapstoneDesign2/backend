package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Adopt;

@Getter
public class ResAdopt {
    private Long id;
    private ResCommon common;
    private AdoptDto adopt;

    public ResAdopt(Adopt adopt) {
        this.id = adopt.getId();
        this.common = ResCommon.of(adopt);
        this.adopt = new AdoptDto(adopt);
    }
}
