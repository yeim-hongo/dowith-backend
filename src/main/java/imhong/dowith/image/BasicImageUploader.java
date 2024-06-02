package imhong.dowith.image;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BasicImageUploader implements ImageUploader {

    @Override
    public String upload(MultipartFile image) {
        return image.getOriginalFilename() + "." + image.getContentType();
    }

    @Override
    public List<String> uploadAll(List<MultipartFile> images) {
        return images.stream()
            .map(this::upload)
            .toList();
    }
}
