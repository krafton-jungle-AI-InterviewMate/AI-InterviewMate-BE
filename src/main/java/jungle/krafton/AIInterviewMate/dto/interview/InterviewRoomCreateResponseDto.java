package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Member;
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
    private String sessionId;
    private String connectionToken;
    private List<InterviewQuestionDto> questionList;

    public InterviewRoomCreateResponseDto(InterviewRoom interviewRoom, Member member) {
        this.roomIdx = interviewRoom.getIdx();
        this.roomName = interviewRoom.getRoomName();
        this.isPrivate = interviewRoom.getIsPrivate();
        this.roomPeopleNum = interviewRoom.getRoomPeopleNum();
        this.roomType = interviewRoom.getRoomType();
        this.nickname = member.getNickname();
        this.roomTime = interviewRoom.getRoomTime();
        this.roomQuestionNum = interviewRoom.getRoomQuestionNum();
        this.roomQuestionBoxIdx = interviewRoom.getRoomQuestionBoxIdx();
        this.createdAt = interviewRoom.getCreatedAt();
        this.roomStatus = interviewRoom.getRoomStatus();
    }

    public void setQuestionList(List<InterviewQuestionDto> questionList) {
        this.questionList = questionList;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setConnectionToken(String connectionToken) {
        this.connectionToken = connectionToken;
    }
}
