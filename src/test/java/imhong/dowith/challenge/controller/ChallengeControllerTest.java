package imhong.dowith.challenge.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.ChallengeRepository;
import imhong.dowith.challenge.domain.ChallengeStatus;
import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.common.ImageFileGenerator;
import imhong.dowith.common.ResponseHeaderHelper;
import imhong.dowith.member.domain.Member;
import imhong.dowith.member.domain.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChallengeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setPort() {
        RestAssured.port = port;
    }

    @BeforeEach
    public void setUp() {
        memberRepository.save(Member.create(
            "yeim0827",
            "nickname",
            "password"
        ));
    }

    @Test
    @Transactional
    void createChallenge() throws IOException {
        // given
        List<MultipartFile> images = List.of(
            ImageFileGenerator.create("image1"),
            ImageFileGenerator.create("image2", MediaType.IMAGE_PNG_VALUE),
            ImageFileGenerator.create("image2", MediaType.IMAGE_GIF_VALUE)
        );
        MultipartFile thumbnail = ImageFileGenerator.create("thumbnail");
        ChallengeCreateRequest request = new ChallengeCreateRequest(
            "create challenge",
            "description",
            "verificationRule",
            thumbnail,
            images,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(10),
            2,
            10
        );
        Member leader = memberRepository.findById(1L).get();

        // when
        Response response = ChallengeControllerSteps.requestCreateChallenge(
            request,
            thumbnail,
            images,
            objectMapper.writeValueAsString(leader)
        );

        // then
        final Long challengeId = ResponseHeaderHelper.getResourceIdFromLocation(
            response.header(LOCATION));
        Challenge challenge = challengeRepository.findById(challengeId).get();

        assertSoftly(softly -> {
                softly.assertThat(response.header(LOCATION)).isEqualTo("/challenges/" + challengeId);
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

                softly.assertThat(challenge.getTitle()).isEqualTo(request.getTitle());
                softly.assertThat(challenge.getStatus()).isEqualTo(ChallengeStatus.NOT_STARTED);
                softly.assertThat(challenge.getParticipantsCount()).isEqualTo(1);
                softly.assertThat(challenge.getThumbnailUrl()).isNotEmpty();
                softly.assertThat(challenge.getImageUrls()).hasSize(images.size());
            }
        );
    }
}