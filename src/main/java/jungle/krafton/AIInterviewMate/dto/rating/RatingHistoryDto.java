package jungle.krafton.AIInterviewMate.dto.rating;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class RatingHistoryDto {
    private String roomName;
    private LocalDateTime createdAt;
    private RoomType roomType;
    private Integer roomTime;
    private Integer roomQuestionNum;

    public RatingHistoryDto(InterviewRoom interviewRoom) {
        this.roomName = interviewRoom.getRoomName();
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomType = interviewRoom.getRoomType();
        this.roomTime = interviewRoom.getRoomTime();
        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
    }
}
