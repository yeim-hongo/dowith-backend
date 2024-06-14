package imhong.dowith.common;

import imhong.dowith.common.image.ImageUploader;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Primary
public class FakeImageUploader implements ImageUploader {

    @Override
    public String upload(MultipartFile image) {
        return "https://" + image.getOriginalFilename() + "." + image.getContentType();
    }

    @Override
    public List<String> uploadAll(List<MultipartFile> images) {
        return images.stream()
            .map(this::upload)
            .toList();
    }
}
