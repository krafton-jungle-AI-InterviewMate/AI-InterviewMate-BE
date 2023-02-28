package jungle.krafton.AIInterviewMate.dto.result;

import jungle.krafton.AIInterviewMate.domain.Result;
import jungle.krafton.AIInterviewMate.domain.Script;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ResultAiResponseDto {
    private String videoUrl;
    private List<String> eyeTimeline;
    private List<String> attitudeTimeline;
    private List<String> questionTimeline;
    private String comment;
    private List<Script> scriptList;

    public ResultAiResponseDto(Result result, List<String> eyeTimeline, List<String> attitudeTimeline, List<String> questionTimeline, List<Script> scripts) {
        this.videoUrl = result.getVideoUrl();
        this.eyeTimeline = eyeTimeline;
        this.attitudeTimeline = attitudeTimeline;
        this.questionTimeline = questionTimeline;
        this.comment = result.getComment();
        this.scriptList = scripts;
    }
}
