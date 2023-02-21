package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRoomCreateResponseDto {
    private Long roomIdx;
    private String roomName;
    private Boolean isPrivate;
    private Integer roomPeopleNum;
    private RoomType roomType;
    private String nickname;
    private Integer roomTime;
    private Integer roomQuestionNum;
    private Long roomQuestionBoxIdx;
    private LocalDateTime createdAt;
    private RoomStatus roomStatus;
    private String connectionToken;
    private List<InterviewQuestionDto> questionList;

    public void setQuestionList(List<InterviewQuestionDto> questionList) {
        this.questionList = questionList;
    }

    public void setConnectionToken(String connectionToken) {
        this.connectionToken = connectionToken;
    }
}
