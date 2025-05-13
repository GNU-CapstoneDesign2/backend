package org.duckdns.petfinderapp.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.duckdns.petfinderapp.domain.post.enums.NeuterStatus;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ADOPT")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Adopt extends PostCommon {

    @Column(name = "animal_num", length = 50)
    private String animalNum;

    private Integer age;
    private String color;
    private String gender;

    @Enumerated(EnumType.STRING)
    private NeuterStatus neuter;

    private Float weight;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "shelter_name", length = 50)
    private String shelterName;

    @Column(name = "shelter_phone", length = 50)
    private String shelterPhone;
}
