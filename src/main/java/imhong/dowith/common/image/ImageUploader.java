package imhong.dowith.common.image;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile image);

    List<String> uploadAll(List<MultipartFile> images);
}
