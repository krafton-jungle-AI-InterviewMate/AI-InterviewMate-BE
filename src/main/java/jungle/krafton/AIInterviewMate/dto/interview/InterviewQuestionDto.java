package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.Question;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InterviewQuestionDto {
    private String questionTitle;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String keyword4;
    private String keyword5;

    public InterviewQuestionDto(Question question) {
        this.questionTitle = question.getQuestionTitle();
        this.keyword1 = question.getKeyword1();
        this.keyword2 = question.getKeyword2();
        this.keyword3 = question.getKeyword3();
        this.keyword4 = question.getKeyword4();
        this.keyword5 = question.getKeyword5();
    }
}
