package jungle.krafton.AIInterviewMate.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ResultInterviewTimelineDto {
    private String type;
    private String timestamp;
}
