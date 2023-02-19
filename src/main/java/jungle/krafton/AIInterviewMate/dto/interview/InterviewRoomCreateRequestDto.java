package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRoomCreateRequestDto {

    private String email;
    private String roomName;
    private Integer roomPeopleNum;
    private String roomPassword;
    private Boolean isPrivate;
    private RoomType roomType;
    private Long roomQuestionBoxIdx;
    private Integer roomQuestionNum;
    private Integer roomTime;

}
