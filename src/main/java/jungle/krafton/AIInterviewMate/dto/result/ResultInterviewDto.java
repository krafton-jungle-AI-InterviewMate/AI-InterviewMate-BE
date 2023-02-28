package jungle.krafton.AIInterviewMate.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ResultInterviewDto {
    private Long roomIdx;
    private String videoUrl;
    private List<String> eyeTimeLines;
    private List<String> attitudeTimeLines;
    private List<String> questionTimeLines;
    private List<ResultInterviewCommentDto> comments;
    private List<ResultInterviewScriptDto> scripts;
}

