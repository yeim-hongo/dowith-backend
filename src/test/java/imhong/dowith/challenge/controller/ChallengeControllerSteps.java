package imhong.dowith.challenge.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.member.domain.Member;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;

public class ChallengeControllerSteps {

    // TODO 인가가 구현되면 leader 파라미터 변경
    public static Response requestCreateChallenge(
        ChallengeCreateRequest request,
        String leader
    ) throws IOException {
        RequestSpecification requestGivenSpec = RestAssured.given()
            .log().all()
            .contentType(MULTIPART_FORM_DATA_VALUE)
            .multiPart("thumbnail", request.getThumbnail().getOriginalFilename(),
                request.getThumbnail().getBytes(),
                request.getThumbnail().getContentType())
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

    public static Response requestParticipate(Member participant, Long id) {
        return RestAssured.given()
            .log().all()
            .contentType("application/json")
            .body(participant)
            .when()
            .post("/challenges/" + id + "/participants")
            .then()
            .log().all()
            .extract()
            .response();
    }
}
