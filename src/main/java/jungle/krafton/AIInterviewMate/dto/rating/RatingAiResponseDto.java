package jungle.krafton.AIInterviewMate.dto.rating;

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
    private Integer eyesRating;
    private Integer attitudeRating;
    private List<RatingAiScriptListDto> scriptList;
    private String roomName;
    private LocalDateTime createdAt;
    private Integer roomQuestionNum;

//    public RatingAiResponseDto(VieweeRating vieweeRating, InterviewRoom interviewRoom, List<RatingAiScriptListDto> scriptList) {
//        this.eyesRating = vieweeRating.getEyesRating();
//        this.attitudeRating = vieweeRating.getAttitudeRating();
//        this.roomName = interviewRoom.getRoomName();
//        this.createdAt = interviewRoom.getCreatedAt();
//        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
//        this.scriptList = scriptList;
//    }
}
