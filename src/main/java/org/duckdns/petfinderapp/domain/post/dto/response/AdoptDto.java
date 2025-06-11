package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Adopt;
import org.duckdns.petfinderapp.domain.post.enums.NeuterStatus;

import java.time.LocalDateTime;

@Getter
public class AdoptDto {
    private String desertionNum; // 구조번호
    private String petNum;

    private String age;
    private String color;
    private String gender;

    private NeuterStatus neuter;
    private Float weight;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String shelterName;
    private String shelterPhone;

    public AdoptDto(Adopt adopt) {
        this.desertionNum = adopt.getDesertionNum();
        this.petNum = adopt.getPetNum();
        this.age = adopt.getAge();
        this.color = adopt.getColor();
        this.gender = adopt.getGender();
        this.neuter = adopt.getNeuter();
        this.weight = adopt.getWeight();
        this.startDate = adopt.getStartDate();
        this.endDate = adopt.getEndDate();
        this.shelterName = adopt.getShelterName();
        this.shelterPhone = adopt.getShelterPhone();
    }
}
