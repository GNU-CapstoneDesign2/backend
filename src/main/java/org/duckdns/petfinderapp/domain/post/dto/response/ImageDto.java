package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Image;

@Getter
@AllArgsConstructor
public class ImageDto {
    private String filePath;

    public ImageDto(Image image) {
        this.filePath = image.getFilePath();
    }
}
