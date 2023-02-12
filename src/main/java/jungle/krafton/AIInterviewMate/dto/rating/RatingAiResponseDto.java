package jungle.krafton.AIInterviewMate.dto.rating;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.VieweeRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class RatingAiResponseDto {
    private int eyesRating;
    private int attitudeRating;
    private int answerRating;
    private List<RatingAiScriptListDto> scriptList;
    private String roomName;
    private LocalDateTime createdAt;
    private int roomQuestionNum;

    public RatingAiResponseDto(VieweeRating vieweeRating, InterviewRoom interviewRoom, List<RatingAiScriptListDto> scriptList) {
        this.eyesRating = vieweeRating.getEyesRating();
        this.attitudeRating = vieweeRating.getAttitudeRating();
        this.answerRating = vieweeRating.getAnswerRating();
        this.roomName = interviewRoom.getRoomName();
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
        this.scriptList = scriptList;
    }
}
