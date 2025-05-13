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

    @Column(name = "animal_num", length = 50)
    private String animalNum;

    @Column(length = 50)
    private String bread;

    @Column(length = 50)
    private String phone;

    private Integer reward;
}

