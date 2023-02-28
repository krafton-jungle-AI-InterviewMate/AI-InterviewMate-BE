package jungle.krafton.AIInterviewMate.dto.result;

import jungle.krafton.AIInterviewMate.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class RatingAiScriptListDto {
    private String questionTitle;
    private String script;
    private Integer rating;

    public RatingAiScriptListDto(Question question, String script, Integer rating) {
        this.questionTitle = question.getQuestionTitle();
        this.script = script;
        this.rating = rating;
    }
}
