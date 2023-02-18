package jungle.krafton.AIInterviewMate.dto.questionbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBoxListDto {
    private Long idx;
    private String boxName;
    private Integer questionNum;
}
