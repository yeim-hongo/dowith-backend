package imhong.dowith.challenge.domain;

import static imhong.dowith.challenge.exception.ChallengeExceptionType.CHALLENGE_START_DATE_TOO_FAR;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.END_DATE_BEFORE_START_DATE;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_DURATION;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_MAX_PARTICIPANTS_COUNT;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_MIN_PARTICIPANTS_COUNT;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.MIN_PARTICIPANTS_EXCEED_MAX_PARTICIPANTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import imhong.dowith.challenge.exception.ChallengeException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ChallengeTest {

    @Test
    void failCreate_WhenEndDateBeforeStartDate() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> createByStartDateAndEndDate(startDate, endDate))
            .satisfies(exception -> {
                ChallengeException challengeException = (ChallengeException) exception;
                assertThat(challengeException.getExceptionType()).isEqualTo(
                    END_DATE_BEFORE_START_DATE);
            });
    }

    @Test
    void failCreate_WhenInvalidDuration() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        // when & then
        assertThatThrownBy(() -> createByStartDateAndEndDate(startDate, endDate))
            .satisfies(exception -> {
                ChallengeException challengeException = (ChallengeException) exception;
                assertThat(challengeException.getExceptionType()).isEqualTo(
                    INVALID_DURATION);
            });
    }

    @Test
    void failCreate_WhenStartDateBeforeToday() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> createByStartDateAndEndDate(startDate, endDate))
            .satisfies(exception -> {
                ChallengeException challengeException = (ChallengeException) exception;
                assertThat(challengeException.getExceptionType()).isEqualTo(
                    END_DATE_BEFORE_START_DATE);
            });
    }

    @Test
    void failCreate_WhenChallengeStartDateTooFar() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(30);
        LocalDate endDate = LocalDate.now().plusDays(31);

        // when & then
        assertThatThrownBy(() -> createByStartDateAndEndDate(startDate, endDate))
            .satisfies(exception -> {
                ChallengeException challengeException = (ChallengeException) exception;
                assertThat(challengeException.getExceptionType()).isEqualTo(
                    CHALLENGE_START_DATE_TOO_FAR);
            });
    }

    @Test
    void failCreate_WhenInvalidMinParticipantsCount() {
        // given
        int minParticipantsCount = 0;

        // when & then
        assertThatThrownBy(() -> createByMinAndMaxParticipantsCount(minParticipantsCount, 10))
            .satisfies(exception -> {
                ChallengeException challengeException = (ChallengeException) exception;
                assertThat(challengeException.getExceptionType()).isEqualTo(
                    INVALID_MIN_PARTICIPANTS_COUNT);
            });
    }

    @Test
    void failCreate_WhenInvalidMaxParticipantsCount() {
        // given
        int maxParticipantsCount = 101;

        // when & then
        assertThatThrownBy(() -> createByMinAndMaxParticipantsCount(1, maxParticipantsCount))
            .satisfies(exception -> {
                ChallengeException challengeException = (ChallengeException) exception;
                assertThat(challengeException.getExceptionType()).isEqualTo(
                    INVALID_MAX_PARTICIPANTS_COUNT);
            });
    }

    @Test
    void failCreate_WhenMinParticipantsCountExceedMaxParticipantsCount() {
        // given
        int minParticipantsCount = 2;
        int maxParticipantsCount = 1;

        // when & then
        assertThatThrownBy(
            () -> createByMinAndMaxParticipantsCount(minParticipantsCount, maxParticipantsCount))
            .satisfies(exception -> {
                ChallengeException challengeException = (ChallengeException) exception;
                assertThat(challengeException.getExceptionType()).isEqualTo(
                    MIN_PARTICIPANTS_EXCEED_MAX_PARTICIPANTS);
            });
    }

    Challenge createByStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        return Challenge.create(
            "하루 일과 작성 챌린지",
            "하루 일과를 작성하는 챌린지입니다.",
            "하루 일과를 기록한 후 사진으로 업로드해주세요.",
            "https://thumbnail.jpg",
            startDate,
            endDate,
            1,
            10,
            1L
        );
    }

    Challenge createByMinAndMaxParticipantsCount(int minParticipantsCount,
        int maxParticipantsCount) {
        return Challenge.create(
            "하루 일과 작성 챌린지",
            "하루 일과를 작성하는 챌린지입니다.",
            "하루 일과를 기록한 후 사진으로 업로드해주세요.",
            "https://thumbnail.jpg",
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            minParticipantsCount,
            maxParticipantsCount,
            1L
        );
    }
}