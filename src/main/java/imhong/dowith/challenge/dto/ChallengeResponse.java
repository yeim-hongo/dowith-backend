package imhong.dowith.challenge.dto;

import imhong.dowith.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ChallengeResponse {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private int participantsCount;
    private int minParticipantsCount;
    private int maxParticipantsCount;
    private String thumbnailUrl;

    // TODO 도메인객체 <-> DTO 논의 필요
    public static ChallengeResponse from(Challenge challenge) {
        return new ChallengeResponse(
            challenge.getTitle(),
            challenge.getDescription(),
            challenge.getStartDate(),
            challenge.getEndDate(),
            challenge.getStatus().toString(),
            challenge.getParticipantsCount(),
            challenge.getMinParticipantsCount(),
            challenge.getMaxParticipantsCount(),
            challenge.getThumbnailUrl()
        );
    }
}
