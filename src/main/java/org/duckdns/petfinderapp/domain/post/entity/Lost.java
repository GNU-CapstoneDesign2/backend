package org.duckdns.petfinderapp.domain.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.duckdns.petfinderapp.domain.post.enums.NeuterStatus;

@Entity
@DiscriminatorValue("LOST")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Lost extends PostCommon {
    private String name;
    private String gender;

    @Enumerated(EnumType.STRING)
    private NeuterStatus neuter;

    @Column(name = "pet_num", length = 50)
    private String petNum;

    @Column(length = 50)
    private String breed;

    @Column(length = 50)
    private String phone;

    private Integer reward;

    public void update(String name, String gender, String petNum, String breed, String phone, Integer reward) {
        this.name = name;
        this.gender = gender;
        this.petNum = petNum;
        this.breed = breed;
        this.phone = phone;
        this.reward = reward;
    }
}

