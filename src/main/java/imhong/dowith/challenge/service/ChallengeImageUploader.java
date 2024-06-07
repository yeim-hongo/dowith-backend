package imhong.dowith.challenge.service;

import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.Image;
import imhong.dowith.image.ImageUploader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ChallengeImageUploader {

    private final static int MAX_IMAGE_COUNT = 5;

    private final ImageUploader imageUploader;

    public String uploadThumbnail(MultipartFile thumbnail) {
        return imageUploader.upload(thumbnail);
    }

    public List<Image> uploadImages(List<MultipartFile> images, Challenge challenge) {
        if (images.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("이미지는 최대 5개까지 업로드할 수 있습니다.");
        }

        return imageUploader.uploadAll(images).stream()
            .map(imageUrl -> Image.create(imageUrl, challenge))
            .toList();
    }
}
