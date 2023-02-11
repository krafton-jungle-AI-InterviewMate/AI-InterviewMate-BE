package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InterviewRoomInfoDto {
    private String roomName;
    private String roomPassword;
    private Integer roomPeopleNum;
    private RoomType roomType;
    private String nickname;
    private Integer roomTime;
    private Integer roomQuestionNum;
    private Long roomQuestionBoxIdx;
    private Long roomViewer1Idx;
    private Long roomViewer2Idx;
    private Long roomViewer3Idx;
    private LocalDateTime createdAt;
    private RoomStatus roomStatus;
    private List<InterviewQuestionDto> questionList;

    public InterviewRoomInfoDto(InterviewRoom interviewRoom, List<InterviewQuestionDto> questions, String memberNickname) {
        this.roomName = interviewRoom.getRoomName();
        this.roomPassword = interviewRoom.getRoomPassword();
        this.roomPeopleNum = interviewRoom.getRoomPeopleNum();
        this.roomType = interviewRoom.getRoomType();
        this.nickname = memberNickname;
        this.roomTime = interviewRoom.getRoomTime();
        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
        this.roomQuestionBoxIdx = interviewRoom.getRoomQuestionBoxIdx();
        this.roomViewer1Idx = interviewRoom.getRoomViewer1Idx();
        this.roomViewer2Idx = interviewRoom.getRoomViewer2Idx();
        this.roomViewer3Idx = interviewRoom.getRoomViewer3Idx();
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomStatus = interviewRoom.getRoomStatus();
        this.questionList = questions;
    }
}
