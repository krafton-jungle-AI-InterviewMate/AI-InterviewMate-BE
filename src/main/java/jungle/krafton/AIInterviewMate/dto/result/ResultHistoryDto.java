package jungle.krafton.AIInterviewMate.dto.result;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ResultHistoryDto {
    private String roomName;
    private Long roomIdx;
    private LocalDateTime createdAt;
    private RoomType roomType;
    private Integer roomTime;
    private Integer roomQuestionNum;

    public ResultHistoryDto(InterviewRoom interviewRoom) {
        this.roomName = interviewRoom.getRoomName();
        this.roomIdx = interviewRoom.getIdx();
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomType = interviewRoom.getRoomType();
        this.roomTime = interviewRoom.getRoomTime();
        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
    }
}
