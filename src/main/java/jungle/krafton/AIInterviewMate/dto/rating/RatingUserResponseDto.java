package jungle.krafton.AIInterviewMate.dto.rating;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
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
public class RatingUserResponseDto {
    private String roomName;
    private LocalDateTime createdAt;
    private int roomTime;
    private List<RatingUserListDto> ratingList;

    public RatingUserResponseDto(InterviewRoom interviewRoom, List<RatingUserListDto> ratingList) {
        this.roomName = interviewRoom.getRoomName();
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomTime = interviewRoom.getRoomTime();
        this.ratingList = ratingList;
    }
}
