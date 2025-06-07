package org.duckdns.petfinderapp.domain.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String fileURL;
    private Long fileSize;

    public void setCommon(PostCommon common) {
        this.common = common;
        if(common == null) return;

        if(!common.getImages().contains(this))
            common.getImages().add(this);
    }

    public static Image of(String url) {
        return Image.builder()
            .origFileName(extractFileName(url))
            .fileURL(url)
            .fileSize(null)
            .build();
    }

    private static String extractFileName(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        }
        return "unknown.jpg"; // 기본 파일명
    }
}
