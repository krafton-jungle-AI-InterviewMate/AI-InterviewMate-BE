package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InterviewRoomInfoDto {
    private String roomName;
    private Boolean isPrivate;
    private String roomPassword;
    private RoomType roomType;
    private String nickname;
    private LocalDateTime createdAt;
    private RoomStatus roomStatus;


    public InterviewRoomInfoDto(InterviewRoom interviewRoom, String memberNickname) {
        this.roomName = interviewRoom.getRoomName();
        this.isPrivate = interviewRoom.getIsPrivate();
        this.roomPassword = interviewRoom.getRoomPassword();
        this.roomType = interviewRoom.getRoomType();
        this.nickname = memberNickname;
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomStatus = interviewRoom.getRoomStatus();
    }
}
