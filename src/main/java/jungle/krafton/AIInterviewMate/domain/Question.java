package jungle.krafton.AIInterviewMate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public Question(Long idx, QuestionBox questionBox, String keyword1, String keyword2, String keyword3, String keyword4, String keyword5, String questionTitle) {
        this.idx = idx;
        this.questionBox = questionBox;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.keyword4 = keyword4;
        this.keyword5 = keyword5;
        this.questionTitle = questionTitle;
    }
}