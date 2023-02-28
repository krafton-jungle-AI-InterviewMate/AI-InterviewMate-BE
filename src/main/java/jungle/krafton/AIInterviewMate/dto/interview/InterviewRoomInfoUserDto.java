package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Member;
import lombok.*;

import java.util.List;

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
        String viewerIdxes = interviewRoom.getInterviewerIdxes();
        List<String> viewerIdxList = List.of(viewerIdxes.split(","));
        this.roomViewer1Idx = Long.valueOf(viewerIdxList.get(0));
        this.roomViewer2Idx = Long.valueOf(viewerIdxList.get(1));
        this.roomViewer3Idx = Long.valueOf(viewerIdxList.get(2));
    }
}
