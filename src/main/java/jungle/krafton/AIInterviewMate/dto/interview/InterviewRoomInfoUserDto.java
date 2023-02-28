package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterviewRoomInfoUserDto extends InterviewRoomCreateResponseDto {
    private List<String> interviewerIdxes;

    public InterviewRoomInfoUserDto(InterviewRoom interviewRoom) {
        super(interviewRoom, interviewRoom.getMember());

        String viewerIdxes = interviewRoom.getInterviewerIdxes();
        this.interviewerIdxes = List.of(viewerIdxes.split(","));
    }
}
