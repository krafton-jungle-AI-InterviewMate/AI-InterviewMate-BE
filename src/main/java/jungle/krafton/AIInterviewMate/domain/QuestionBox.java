package jungle.krafton.AIInterviewMate.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class QuestionBox {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "member_idx")
    private Member member;

    @Column(nullable = false)
    private String boxName;

    @Column(nullable = false)
    private Integer questionNum;

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }
}
