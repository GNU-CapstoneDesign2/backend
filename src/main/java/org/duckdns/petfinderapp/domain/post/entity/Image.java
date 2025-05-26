package org.duckdns.petfinderapp.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Builder
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "common_id")
    private PostCommon common;

    private String origFileName;
    private String filePath;
    private Long fileSize;

    public void setCommon(PostCommon common) {
        this.common = common;
        if(common == null) return;

        if(!common.getImages().contains(this))
            common.getImages().add(this);
    }
}
