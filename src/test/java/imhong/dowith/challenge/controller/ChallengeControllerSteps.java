package imhong.dowith.challenge.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ChallengeControllerSteps {

    // TODO 인가가 구현되면 leader 파라미터 변경
    public static Response requestCreateChallenge(
        ChallengeCreateRequest request,
        MultipartFile thumbnail,
        List<MultipartFile> images,
        String leader
    ) throws IOException {
        RequestSpecification requestGivenSpec = RestAssured.given()
            .log().all()
            .contentType(MULTIPART_FORM_DATA_VALUE)
            .multiPart("thumbnail", thumbnail.getOriginalFilename(), thumbnail.getBytes(),
                thumbnail.getContentType())
            .multiPart("title", request.getTitle())
            .multiPart("description", request.getDescription())
            .multiPart("verificationRule", request.getVerificationRule())
            .multiPart("startDate", request.getStartDate().toString())
            .multiPart("endDate", request.getEndDate().toString())
            .multiPart("minParticipantsCount", request.getMinParticipantsCount())
            .multiPart("maxParticipantsCount", request.getMaxParticipantsCount())
            .multiPart("leader", leader, "application/json");

        request.getImages().forEach(image -> {
            try {
                requestGivenSpec.multiPart("images", image.getOriginalFilename(), image.getBytes(),
                    image.getContentType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return requestGivenSpec
            .contentType("multipart/form-data")
            .when()
            .post("/challenges")
            .then()
            .log().all()
            .extract()
            .response();
    }
}
