package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterviewRoomInfoUserDto extends InterviewRoomCreateResponseDto {
    private Long roomViewer1Idx;
    private Long roomViewer2Idx;
    private Long roomViewer3Idx;

    public InterviewRoomInfoUserDto(InterviewRoom interviewRoom, Member member) {
        super(interviewRoom, member);
        this.roomViewer1Idx = interviewRoom.getRoomViewer1Idx();
        this.roomViewer2Idx = interviewRoom.getRoomViewer2Idx();
        this.roomViewer3Idx = interviewRoom.getRoomViewer3Idx();
    }
}
