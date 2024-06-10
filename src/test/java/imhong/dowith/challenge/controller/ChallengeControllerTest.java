package imhong.dowith.challenge.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.ChallengeStatus;
import imhong.dowith.challenge.domain.Image;
import imhong.dowith.challenge.domain.MemberChallenge;
import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.challenge.repository.ChallengeRepository;
import imhong.dowith.challenge.repository.ImageRepository;
import imhong.dowith.challenge.repository.MemberChallengeRepository;
import imhong.dowith.common.ControllerTest;
import imhong.dowith.common.ImageFileGenerator;
import imhong.dowith.common.ResponseHeaderHelper;
import imhong.dowith.member.domain.Member;
import imhong.dowith.member.repository.MemberRepository;
import io.restassured.response.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

class ChallengeControllerTest extends ControllerTest {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberChallengeRepository memberChallengeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        memberRepository.save(Member.create(
            "yeim0827",
            "nickname",
            "password"
        ));
    }

    @Test
    void createChallenge() throws IOException {
        // given
        List<MultipartFile> imageFiles = List.of(
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
            imageFiles,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(10),
            2,
            10
        );
        Member leader = memberRepository.findById(1L).get();

        // when
        Response response = ChallengeControllerSteps.requestCreateChallenge(
            request,
            objectMapper.writeValueAsString(leader)
        );

        // then
        final Long challengeId = ResponseHeaderHelper.getResourceIdFromLocation(
            response.header(LOCATION));
        Challenge challenge = challengeRepository.findById(challengeId).get();
        List<Image> images = imageRepository.findAllByChallengeId(challengeId);

        assertSoftly(softly -> {
                softly.assertThat(response.header(LOCATION)).isEqualTo("/challenges/" + challengeId);
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

                softly.assertThat(challenge.getTitle()).isEqualTo(request.getTitle());
                softly.assertThat(challenge.getStatus()).isEqualTo(ChallengeStatus.NOT_STARTED);
                softly.assertThat(challenge.getParticipantsCount()).isEqualTo(1);
                softly.assertThat(challenge.getThumbnailUrl()).isNotEmpty();
                softly.assertThat(images).hasSize(imageFiles.size());
            }
        );
    }

    @Test
    void participate() {
        // given
        Member leader = memberRepository.findById(1L).get();
        Challenge challenge = challengeRepository.save(Challenge.create(
            "create challenge",
            "description",
            "verificationRule",
            "thumbnailUrl",
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(10),
            2,
            10,
            leader.getId()
        ));
        Member participant = memberRepository.save(Member.create(
            "participant",
            "nickname2",
            "password2"
        ));

        // when
        Response response = ChallengeControllerSteps.requestParticipate(
            participant,
            challenge.getId()
        );

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            softly.assertThat(
                    challengeRepository.findById(challenge.getId()).get().getParticipantsCount())
                .isEqualTo(2);

            MemberChallenge memberChallenge = memberChallengeRepository.findByMemberId(
                participant.getId()).get();
            softly.assertThat(memberChallenge.getChallengeId()).isEqualTo(challenge.getId());
        });
    }
}
