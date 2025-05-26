package org.duckdns.petfinderapp.domain.post.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LostUpdate {
    @JsonProperty("common")
    private CommonUpdate commonUpdate;
    @JsonProperty("lost")
    private ReqLost reqLost;

    @Builder
    public LostUpdate(CommonUpdate commonUpdate, ReqLost reqLost) {
        this.commonUpdate = commonUpdate;
        this.reqLost = reqLost;
    }
}
