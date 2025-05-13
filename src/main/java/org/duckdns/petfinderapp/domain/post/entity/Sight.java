package org.duckdns.petfinderapp.domain.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SIGHT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Sight extends PostCommon {
}

