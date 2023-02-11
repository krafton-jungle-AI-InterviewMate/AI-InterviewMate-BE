package jungle.krafton.AIInterviewMate.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CommentsRequestDto {
    private Long viewerIdx;
    private String questionTitle;
    private String comment;
}
