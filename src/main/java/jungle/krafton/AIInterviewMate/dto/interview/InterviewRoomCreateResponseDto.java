package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRoomCreateResponseDto {
    private Long roomIdx;
    private String roomName;
    private Integer roomPeopleNum;
    private String roomPassword;
    private RoomType roomType;
    private String nickname;
    private Integer roomTime;
    private Integer roomQuestionNum;
    private Long roomQuestionBoxIdx;
    private LocalDateTime createdAt;
    private RoomStatus roomStatus;
}
