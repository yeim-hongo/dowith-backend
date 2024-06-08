package imhong.dowith.challenge.service;

import static imhong.dowith.challenge.exception.ChallengeExceptionType.CHALLENGE_NOT_FOUND;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.CHALLENGE_START_DATE_TOO_FAR;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.END_DATE_BEFORE_START_DATE;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_DURATION;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_MAX_PARTICIPANTS_COUNT;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_MIN_PARTICIPANTS_COUNT;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.MIN_PARTICIPANTS_EXCEED_MAX_PARTICIPANTS;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.PARTICIPANTS_COUNT_FULL;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.START_DATE_BEFORE_TODAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.ChallengeStatus;
import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.challenge.exception.ChallengeException;
import imhong.dowith.challenge.repository.ChallengeRepository;
import imhong.dowith.challenge.repository.ImageRepository;
import imhong.dowith.challenge.repository.MemberChallengeRepository;
import imhong.dowith.common.FakeImageUploader;
import imhong.dowith.common.ImageFileGenerator;
import imhong.dowith.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChallengeServiceTest {

    @Spy
    private ChallengeImageUploader challengeImageUploader = new ChallengeImageUploader(
        new FakeImageUploader()
    );

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private MemberChallengeRepository memberChallengeRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ChallengeService challengeService;

    @Nested
    class CreateChallenge {

        private Member member = new Member(
            1L,
            "userId",
            "nickname",
            "password"
        );

        @Test
        void successCreateChallenge() {
            // given
            List<MultipartFile> images = List.of(
                ImageFileGenerator.create("image1"),
                ImageFileGenerator.create("image2"),
                ImageFileGenerator.create("image3")
            );
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                images,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                1,
                5
            );
            given(challengeRepository.save(any())).willReturn(new Challenge(
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
            ));
            given(imageRepository.saveAll(any())).willReturn(List.of());
            given(memberChallengeRepository.save(any())).willReturn(null);

            // when
            Long id = challengeService.createChallenge(member, request);

            // then
            assertThat(id).isEqualTo(1);
            verify(challengeImageUploader).uploadThumbnail(request.getThumbnail());
            verify(challengeImageUploader).uploadImages(anyList(), any(Challenge.class));
            verify(challengeRepository).save(any(Challenge.class));
            verify(imageRepository).saveAll(any());
            verify(memberChallengeRepository).save(any());
        }

        @Test
        void failCreateChallenge_WhenEndDateBeforeStartDate() {
            // given
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                null,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(2),
                1,
                5
            );

            // when & then
            assertThatThrownBy(() -> challengeService.createChallenge(member, request))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        END_DATE_BEFORE_START_DATE);
                });
        }

        @Test
        void failCreateChallenge_WhenInvalidDuration() {
            // given
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                null,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(30),
                1,
                5
            );

            // when & then
            assertThatThrownBy(() -> challengeService.createChallenge(member, request))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        INVALID_DURATION);
                });
        }

        @Test
        void failCreateChallenge_WhenStartDateBeforeToday() {
            // given
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                null,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(2),
                1,
                5
            );

            // when & then
            assertThatThrownBy(() -> challengeService.createChallenge(member, request))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        START_DATE_BEFORE_TODAY);
                });
        }

        @Test
        void failCreateChallenge_WhenChallengeStartDateTooFar() {
            // given
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                null,
                LocalDate.now().plusDays(30),
                LocalDate.now().plusDays(31),
                1,
                5
            );

            // when & then
            assertThatThrownBy(() -> challengeService.createChallenge(member, request))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        CHALLENGE_START_DATE_TOO_FAR);
                });
        }

        @Test
        void failCreateChallenge_WhenInvalidMinParticipantsCount() {
            // given
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                0,
                5
            );

            // when & then
            assertThatThrownBy(() -> challengeService.createChallenge(member, request))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        INVALID_MIN_PARTICIPANTS_COUNT);
                });
        }

        @Test
        void failCreateChallenge_WhenInvalidMaxParticipantsCount() {
            // given
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                1,
                101
            );

            // when & then
            assertThatThrownBy(() -> challengeService.createChallenge(member, request))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        INVALID_MAX_PARTICIPANTS_COUNT);
                });
        }

        @Test
        void failCreateChallenge_WhenMinParticipantsCountExceedMaxParticipantsCount() {
            // given
            ChallengeCreateRequest request = new ChallengeCreateRequest(
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                ImageFileGenerator.create("thumbnail"),
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                2,
                1
            );

            // when & then
            assertThatThrownBy(() -> challengeService.createChallenge(member, request))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        MIN_PARTICIPANTS_EXCEED_MAX_PARTICIPANTS);
                });
        }
    }

    @Nested
    class Participate {

        private Member participant = new Member(
            2L,
            "userId",
            "nickname",
            "password"
        );
        private Challenge challenge = new Challenge(
            1L,
            "하루 일과 작성 챌린지",
            "하루 일과를 작성하는 챌린지입니다.",
            "하루 일과를 기록한 후 사진으로 업로드해주세요.",
            "https://thumbnail.jpg",
            null,
            LocalDate.now().plusDays(1),
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
        void successParticipate() {
            // given
            given(challengeRepository.findById(1L)).willReturn(java.util.Optional.of(challenge));
            given(memberChallengeRepository.save(any())).willReturn(null);

            // when
            challengeService.participate(participant, challenge.getId());

            // then
            assertThat(challenge.getParticipantsCount()).isEqualTo(2);
            verify(challengeRepository).findById(1L);
            verify(memberChallengeRepository).save(any());
        }

        @Test
        void failParticipate_WhenParticipantsCountFull() {
            // given
            Challenge challenge = new Challenge(
                1L,
                "하루 일과 작성 챌린지",
                "하루 일과를 작성하는 챌린지입니다.",
                "하루 일과를 기록한 후 사진으로 업로드해주세요.",
                "https://thumbnail.jpg",
                null,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                ChallengeStatus.NOT_STARTED,
                10,
                1,
                10,
                1L,
                null,
                null,
                null
            );
            given(challengeRepository.findById(1L)).willReturn(java.util.Optional.of(challenge));

            // when & then
            assertThatThrownBy(() -> challengeService.participate(participant, challenge.getId()))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        PARTICIPANTS_COUNT_FULL);
                });
        }

        @Test
        void failParticipate_WhenChallengeNotFound() {
            // given
            given(challengeRepository.findById(1L)).willReturn(java.util.Optional.empty());

            // when & then
            assertThatThrownBy(() -> challengeService.participate(participant, 1L))
                .satisfies(exception -> {
                    ChallengeException challengeException = (ChallengeException) exception;
                    assertThat(challengeException.getExceptionType()).isEqualTo(
                        CHALLENGE_NOT_FOUND);
                });
        }
    }
}