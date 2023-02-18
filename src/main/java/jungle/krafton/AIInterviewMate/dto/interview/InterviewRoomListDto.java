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
public class InterviewRoomListDto {

    private Long Idx;
    private String roomName;
    private String nickName;
    private Integer roomPeopleNum;
    private Integer roomPeopleNow;
    private Integer roomTime;
    private Boolean roomIsPrivate;
    private RoomType roomType;
    private RoomStatus roomStatus;
    private LocalDateTime createdAt;

}