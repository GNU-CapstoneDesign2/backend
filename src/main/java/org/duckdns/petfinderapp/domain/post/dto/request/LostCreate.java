package org.duckdns.petfinderapp.domain.post.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LostCreate {
    @JsonProperty("common")
    private CommonCreate commonCreate;
    @JsonProperty("lost")
    private ReqLost reqLost;

    @Builder
    public LostCreate(CommonCreate common, ReqLost lost) {
        this.commonCreate = common;
        this.reqLost = lost;
    }
}
