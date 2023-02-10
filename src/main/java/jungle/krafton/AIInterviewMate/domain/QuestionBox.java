package jungle.krafton.AIInterviewMate.domain;

import lombok.Builder;
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

    @Builder
    public QuestionBox(Long idx, Member member, String boxName, int questionNum) {
        this.idx = idx;
        this.member = member;
        this.boxName = boxName;
        this.questionNum = questionNum;
    }
}
