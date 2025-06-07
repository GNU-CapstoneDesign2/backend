package org.duckdns.petfinderapp.domain.post.entity;

import java.time.LocalDateTime;

import org.duckdns.petfinderapp.domain.post.enums.NeuterStatus;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ADOPT")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Adopt extends PostCommon {

    @Column(name = "animal_num", length = 50)
    private String animalNum;

    private String age;
    private String color;
    private String gender;

    @Enumerated(EnumType.STRING)
    private NeuterStatus neuter;

    private Float weight; // 체중 (Kg 단위)

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; //공고 시작일

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate; //공고 종료일

    @Column(name = "shelter_name", length = 50)
    private String shelterName;

    @Column(name = "shelter_phone", length = 50)
    private String shelterPhone;

    public void updateWith(Adopt adopt) {

        super.update(
            adopt.getDate(),
            adopt.getAddress(),
            adopt.getCoordinates(),
            adopt.getPetType(),
            adopt.getContent(),
            adopt.getState()
        );

        this.animalNum = adopt.getAnimalNum();
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
