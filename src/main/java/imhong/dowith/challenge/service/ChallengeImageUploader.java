package imhong.dowith.challenge.service;

import static imhong.dowith.challenge.enums.ChallengeExceptionType.IMAGES_SIZE_EXCEEDED;

import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.Image;
import imhong.dowith.common.CustomException;
import imhong.dowith.image.ImageUploader;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ChallengeImageUploader {

    public final static int MAX_IMAGE_COUNT = 5;

    private ImageUploader imageUploader;

    public String uploadThumbnail(MultipartFile thumbnail) {
        return imageUploader.upload(thumbnail);
    }

    public List<Image> uploadImages(List<MultipartFile> images, Challenge challenge) {
        if (images.size() > MAX_IMAGE_COUNT) {
            throw new CustomException(IMAGES_SIZE_EXCEEDED);
        }

        return imageUploader.uploadAll(images).stream()
            .map(imageUrl -> Image.create(imageUrl, challenge))
            .toList();
    }
}
