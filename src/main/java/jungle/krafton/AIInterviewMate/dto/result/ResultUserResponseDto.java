package jungle.krafton.AIInterviewMate.dto.result;

import jungle.krafton.AIInterviewMate.domain.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class ResultUserResponseDto {
    private String videoUrl;
    private List<String> eyeTimeline;
    private List<String> attitudeTimeline;
    private List<String> questionTimeline;
    private String comment;
    private List<ResultInterviewCommentDto> comments;

    public ResultUserResponseDto(Result result, List<String> eyeTimeline, List<String> attitudeTimeline, List<String> questionTimeline, List<ResultInterviewCommentDto> comments) {
        this.videoUrl = result.getVideoUrl();
        this.eyeTimeline = eyeTimeline;
        this.attitudeTimeline = attitudeTimeline;
        this.questionTimeline = questionTimeline;
        this.comment = result.getComment();
        this.comments = comments;
    }
}
