package org.duckdns.petfinderapp.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.enums.NeuterStatus;

@Getter
@Builder
public class ReqLost {
    private String name;
    private String gender;
    private NeuterStatus neuter;
    private String petNum;
    private String breed;
    private String phone;
    private Integer reward;
}