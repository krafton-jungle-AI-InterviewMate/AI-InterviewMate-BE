package jungle.krafton.AIInterviewMate.dto.result;

import jungle.krafton.AIInterviewMate.domain.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ResultAiResponseDto {
    private String videoUrl;
    private List<String> eyeTimeline;
    private List<String> attitudeTimeline;
    private List<String> questionTimeline;
    private String memo;
    private List<ResultInterviewScriptDto> scripts;

    public ResultAiResponseDto(Result result, List<String> eyeTimeline, List<String> attitudeTimeline, List<String> questionTimeline, List<ResultInterviewScriptDto> scripts) {
        this.videoUrl = result.getVideoUrl();
        this.eyeTimeline = eyeTimeline;
        this.attitudeTimeline = attitudeTimeline;
        this.questionTimeline = questionTimeline;
        this.memo = result.getMemo();
        this.scripts = scripts;
    }
}
