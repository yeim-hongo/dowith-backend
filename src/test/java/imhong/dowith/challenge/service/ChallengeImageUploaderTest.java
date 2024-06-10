package imhong.dowith.challenge.service;

import static imhong.dowith.challenge.enums.ChallengeExceptionType.IMAGES_SIZE_EXCEEDED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.ChallengeStatus;
import imhong.dowith.challenge.domain.Image;
import imhong.dowith.common.CustomException;
import imhong.dowith.common.FakeImageUploader;
import imhong.dowith.common.ImageFileGenerator;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChallengeImageUploaderTest {

    private ChallengeImageUploader challengeImageUploader = new ChallengeImageUploader(
        new FakeImageUploader());

    @Test
    void uploadThumbnail() {
        // given
        MultipartFile thumbnail = ImageFileGenerator.create("thumbnail");

        // when
        String imageUrl = challengeImageUploader.uploadThumbnail(thumbnail);

        // then
        // TODO 업로드 이미지 url 테스트 추가 필요
        assertThat(imageUrl).isNotBlank();
    }

    @Nested
    class UploadImages {

        private Challenge challenge = new Challenge(
            1L,
            "하루 일과 작성 챌린지",
            "하루 일과를 작성하는 챌린지입니다.",
            "하루 일과를 기록한 후 사진으로 업로드해주세요.",
            "https://thumbnail.jpg",
            null,
            LocalDate.now(),
            LocalDate.now().plusDays(2),
            ChallengeStatus.NOT_STARTED,
            1,
            1,
            10,
            1L,
            null,
            null,
            null
        );

        @Test
        void successUploadImages() {
            // given
            MultipartFile image1 = ImageFileGenerator.create("image1");
            MultipartFile image2 = ImageFileGenerator.create("image2");
            MultipartFile image3 = ImageFileGenerator.create("image3");
            List<MultipartFile> images = List.of(image1, image2, image3);

            // when
            List<Image> uploadImages = challengeImageUploader.uploadImages(images, challenge);

            //then
            assertSoftly(softly -> {
                softly.assertThat(uploadImages).hasSize(3);
                uploadImages.forEach(image -> softly.assertThat(image.getUrl()).isNotBlank());
                uploadImages.forEach(
                    image -> softly.assertThat(image.getChallenge()).isEqualTo(challenge));
            });
        }

        @Test
        void failUploadImages_WhenImagesSizeExceeded() {
            // given
            MultipartFile image1 = ImageFileGenerator.create("image1");
            MultipartFile image2 = ImageFileGenerator.create("image2");
            MultipartFile image3 = ImageFileGenerator.create("image3");
            MultipartFile image4 = ImageFileGenerator.create("image4");
            MultipartFile image5 = ImageFileGenerator.create("image5");
            MultipartFile image6 = ImageFileGenerator.create("image6");
            List<MultipartFile> images = List.of(image1, image2, image3, image4, image5, image6);

            // when & then
            assertThatThrownBy(() -> challengeImageUploader.uploadImages(images, challenge))
                .satisfies(exception -> {
                    CustomException challengeException = (CustomException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        IMAGES_SIZE_EXCEEDED);
                });
        }
    }
}
