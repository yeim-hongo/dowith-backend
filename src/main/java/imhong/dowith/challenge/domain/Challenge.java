package imhong.dowith.challenge.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
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
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 빨라야 합니다.");
        }

        if (startDate.plusDays(1).isAfter(endDate)) {
            throw new IllegalArgumentException("챌린지 기간은 1일 이상이어야 합니다.");
        }

        if (startDate.plusWeeks(4).isBefore(endDate)) {
            throw new IllegalArgumentException("챌린지 기간은 4주 이하이어야 합니다.");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("챌린지 시작 날짜는 현재 날짜보다 빨라야 합니다.");
        }

        if (startDate.isAfter(LocalDate.now().plusWeeks(4))) {
            throw new IllegalArgumentException("챌린지 시작 날짜는 현재 날짜로부터 4주 이후로 설정할 수 없습니다.");
        }
    }

    private static void validateParticipantsCount(Integer minParticipantsCount,
        Integer maxParticipantsCount) {
        if (minParticipantsCount < 1) {
            throw new IllegalArgumentException("최소 참가자 수는 1명 이상이어야 합니다.");
        }

        if (maxParticipantsCount > 100) {
            throw new IllegalArgumentException("최대 참가자 수는 100명 이하이어야 합니다.");
        }

        if (minParticipantsCount > maxParticipantsCount) {
            throw new IllegalArgumentException("최소 참가자 수는 최대 참가자 수보다 작아야 합니다.");
        }
    }

    public boolean isFull() {
        return participantsCount >= maxParticipantsCount;
    }

    public void increaseParticipantsCount() {
        participantsCount++;
    }
}
