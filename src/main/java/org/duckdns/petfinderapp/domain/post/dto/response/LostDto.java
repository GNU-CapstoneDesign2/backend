package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Lost;

@Getter
public class LostDto {
    private String name;
    private String gender;
    private String petNum;
    private String breed;
    private String phone;
    private Integer reward;

    public LostDto(Lost lost) {
        this.name = lost.getName();
        this.gender = lost.getGender();
        this.petNum = lost.getPetNum();
        this.breed = lost.getBreed();
        this.phone = lost.getPhone();
        this.reward = lost.getReward();
    }
}
