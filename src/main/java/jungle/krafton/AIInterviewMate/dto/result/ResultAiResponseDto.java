package jungle.krafton.AIInterviewMate.dto.result;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ResultAiResponseDto {
    private String videoUrl;
    private List<ResultTimelineDto> timelines;
    private String memo;
    private List<ResultResponseScriptDto> scripts;
    private LocalDateTime createdAt;
    private String roomName;
    private Integer roomQuestionNum;


    public ResultAiResponseDto(Result result, List<ResultTimelineDto> timelines, List<ResultResponseScriptDto> scripts, InterviewRoom interviewRoom) {
        this.videoUrl = result.getVideoUrl();
        this.timelines = timelines;
        this.memo = result.getMemo();
        this.scripts = scripts;
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomName = interviewRoom.getRoomName();
        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
    }
}
