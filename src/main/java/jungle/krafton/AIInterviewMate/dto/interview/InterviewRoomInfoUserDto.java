package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterviewRoomInfoUserDto extends InterviewRoomInfoDto {
    private Integer roomPeopleNum;
    private Integer roomTime;
    private Long roomViewer1Idx;
    private Long roomViewer2Idx;
    private Long roomViewer3Idx;

    public InterviewRoomInfoUserDto(InterviewRoom interviewRoom, String memberNickname) {
        super(interviewRoom, memberNickname);
        this.roomPeopleNum = interviewRoom.getRoomPeopleNum();
        this.roomTime = interviewRoom.getRoomTime();
        this.roomViewer1Idx = interviewRoom.getRoomViewer1Idx();
        this.roomViewer2Idx = interviewRoom.getRoomViewer2Idx();
        this.roomViewer3Idx = interviewRoom.getRoomViewer3Idx();
    }
}
