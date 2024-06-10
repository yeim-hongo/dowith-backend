package imhong.dowith.common;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileGenerator {

    public static MultipartFile create(String fileName) {
        return new MockMultipartFile(
            fileName,
            fileName,
            MediaType.IMAGE_JPEG_VALUE,
            fileName.getBytes()
        );
    }

    public static MultipartFile create(String fileName, String contentType) {
        return new MockMultipartFile(
            fileName,
            fileName,
            contentType,
            fileName.getBytes()
        );
    }

    public static MultipartFile create(String fileName, String contentType,
        byte[] content) {
        return new MockMultipartFile(
            fileName,
            fileName,
            contentType,
            content
        );
    }
}
