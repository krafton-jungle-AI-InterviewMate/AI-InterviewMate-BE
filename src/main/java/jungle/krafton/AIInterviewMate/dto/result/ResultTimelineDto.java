package jungle.krafton.AIInterviewMate.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResultTimelineDto {
    private String type;
    private String timestamp;
}