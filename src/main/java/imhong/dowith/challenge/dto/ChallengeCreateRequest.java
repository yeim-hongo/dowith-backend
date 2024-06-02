package imhong.dowith.challenge.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ChallengeCreateRequest {

    @NotNull(message = "챌린지 제목을 입력해주세요.")
    private String title;

    @NotNull(message = "챌린지 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "챌린지 인증 규칙을 입력해주세요.")
    private String verificationRule;

    private MultipartFile thumbnail;

    private List<MultipartFile> images;

    @NotNull(message = "챌린지 시작 날짜를 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "챌린지 종료 날짜를 입력해주세요.")
    private LocalDate endDate;

    private Integer minParticipantsCount;

    private Integer maxParticipantsCount;
}
