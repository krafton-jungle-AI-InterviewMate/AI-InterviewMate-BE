package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterviewRoomInfoAiDto extends InterviewRoomInfoDto {
    private Integer roomQuestionNum;
    private Long roomQuestionBoxIdx;
    private List<InterviewQuestionDto> questionList;

    public InterviewRoomInfoAiDto(InterviewRoom interviewRoom, String memberNickname, List<InterviewQuestionDto> questions) {
        super(interviewRoom, memberNickname);
        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
        this.roomQuestionBoxIdx = interviewRoom.getRoomQuestionBoxIdx();
        this.questionList = questions;
    }
}
