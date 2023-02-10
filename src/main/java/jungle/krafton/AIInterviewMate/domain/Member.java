package jungle.krafton.AIInterviewMate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @OneToMany(mappedBy = "member")
    private final List<InterviewRoom> interviewRoomList = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;
    @Column(nullable = false)
    private String nickname;

//    @OneToMany(mappedBy = "member")
//    private List<QuestionBox> questionBoxList = new ArrayList<>();
    @Column(nullable = false)
    private String email;

    @Builder
    public Member(Long idx, String nickname, String email) {
        this.idx = idx;
        this.nickname = nickname;
        this.email = email;
    }
}
