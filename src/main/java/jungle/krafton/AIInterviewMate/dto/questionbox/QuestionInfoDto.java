package jungle.krafton.AIInterviewMate.dto.questionbox;

import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.domain.QuestionBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QuestionInfoDto {
    private Long questionIdx;

    private String questionTitle;

    private String keyword1;

    private String keyword2;

    private String keyword3;

    private String keyword4;

    private String keyword5;


    public static QuestionInfoDto of(Question question) {
        return QuestionInfoDto.builder()
                .questionIdx(question.getIdx())
                .questionTitle(question.getQuestionTitle())
                .keyword1(question.getKeyword1())
                .keyword2(question.getKeyword2())
                .keyword3(question.getKeyword3())
                .keyword4(question.getKeyword4())
                .keyword5(question.getKeyword5())
                .build();
    }

    public Question ConvertToQuestionWithQuestionBox(QuestionBox questionBox) {
        return Question.builder()
                .questionBox(questionBox)
                .questionTitle(this.questionTitle)
                .keyword1(this.keyword1)
                .keyword2(this.keyword2)
                .keyword3(this.keyword3)
                .keyword4(this.keyword4)
                .keyword5(this.keyword5)
                .build();
    }
}
