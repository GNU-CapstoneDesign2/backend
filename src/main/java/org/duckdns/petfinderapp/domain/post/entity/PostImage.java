package org.duckdns.petfinderapp.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_image_seq")
    @SequenceGenerator(
            name = "post_image_seq",
            sequenceName = "post_image_seq",
            allocationSize = 1
    )
    private Long imageId;    // → post_image.image_id

    /**
     * 어떤 게시글의 이미지인지
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostCommon post; // → post_image.post_id

    /**
     * 이미지 URL
     */
    @Column(name = "image_url",
            nullable = false,
            columnDefinition = "TEXT")
    private String imageUrl; // → post_image.image_url
}
