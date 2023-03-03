package jungle.krafton.AIInterviewMate.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ResultInterviewDto {
    private List<String> eyeTimelines;
    private List<String> attitudeTimelines;
    private List<String> questionTimelines;
    private List<ResultInterviewCommentDto> comments;
    private List<ResultInterviewScriptDto> scripts;
}

