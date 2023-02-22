package jungle.krafton.AIInterviewMate.domain;

import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "questionbox_idx")
    private QuestionBox questionBox;

    @Column(nullable = false)
    private String keyword1;

    @Column()
    private String keyword2;

    @Column()
    private String keyword3;

    @Column()
    private String keyword4;

    @Column()
    private String keyword5;

    @Column(nullable = false)
    private String questionTitle;

    public void update(QuestionInfoDto questionInfoDto) {
        this.keyword1 = questionInfoDto.getKeyword1();
        this.keyword2 = questionInfoDto.getKeyword2();
        this.keyword3 = questionInfoDto.getKeyword3();
        this.keyword4 = questionInfoDto.getKeyword4();
        this.keyword5 = questionInfoDto.getKeyword5();
        this.questionTitle = questionInfoDto.getQuestionTitle();
    }
}
