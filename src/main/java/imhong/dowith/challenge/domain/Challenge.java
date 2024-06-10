package imhong.dowith.challenge.domain;

import static imhong.dowith.challenge.exception.ChallengeExceptionType.CHALLENGE_START_DATE_TOO_FAR;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.END_DATE_BEFORE_START_DATE;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_DURATION;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_MAX_PARTICIPANTS_COUNT;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.INVALID_MIN_PARTICIPANTS_COUNT;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.MIN_PARTICIPANTS_EXCEED_MAX_PARTICIPANTS;
import static imhong.dowith.challenge.exception.ChallengeExceptionType.START_DATE_BEFORE_TODAY;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import imhong.dowith.challenge.exception.ChallengeException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Challenge {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String verificationRule;

    private String thumbnailUrl;

    @OneToMany(mappedBy = "challenge")
    private List<Image> imageUrls;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @Enumerated(STRING)
    private ChallengeStatus status;

    @Column(nullable = false)
    private Integer participantsCount;

    @Column(nullable = false)
    private Integer minParticipantsCount;

    @Column(nullable = false)
    private Integer maxParticipantsCount;

    @Column(nullable = false)
    private Long leaderMemberId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static Challenge create(
        String title,
        String description,
        String verificationRule,
        String thumbnailUrl,
        LocalDate startDate,
        LocalDate endDate,
        Integer minParticipantsCount,
        Integer maxParticipantsCount,
        Long leaderMemberId
    ) {
        validateDuration(startDate, endDate);
        validateParticipantsCount(minParticipantsCount, maxParticipantsCount);

        return new Challenge(
            null,
            title,
            description,
            verificationRule,
            thumbnailUrl,
            null,
            startDate,
            endDate,
            ChallengeStatus.NOT_STARTED,
            1,
            minParticipantsCount,
            maxParticipantsCount,
            leaderMemberId,
            null,
            null,
            null
        );
    }

    private static void validateDuration(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ChallengeException(END_DATE_BEFORE_START_DATE);
        }

        if (startDate.plusWeeks(4).isBefore(endDate)) {
            throw new ChallengeException(INVALID_DURATION);
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new ChallengeException(START_DATE_BEFORE_TODAY);
        }

        if (startDate.isAfter(LocalDate.now().plusWeeks(4))) {
            throw new ChallengeException(CHALLENGE_START_DATE_TOO_FAR);
        }
    }

    private static void validateParticipantsCount(Integer minParticipantsCount,
        Integer maxParticipantsCount) {
        if (minParticipantsCount < 1) {
            throw new ChallengeException(INVALID_MIN_PARTICIPANTS_COUNT);
        }

        if (maxParticipantsCount > 100) {
            throw new ChallengeException(INVALID_MAX_PARTICIPANTS_COUNT);
        }

        if (minParticipantsCount > maxParticipantsCount) {
            throw new ChallengeException(MIN_PARTICIPANTS_EXCEED_MAX_PARTICIPANTS);
        }
    }

    public boolean isFull() {
        return participantsCount >= maxParticipantsCount;
    }

    public void increaseParticipantsCount() {
        participantsCount++;
    }
}
