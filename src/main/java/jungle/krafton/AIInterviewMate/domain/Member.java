package jungle.krafton.AIInterviewMate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

//    @OneToMany(mappedBy = "member")
//    private List<QuestionBox> questionBoxList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<InterviewRoom> interviewRoomList = new ArrayList<>();

    @Builder
    public Member(Long idx, String nickname, String email) {
        this.idx = idx;
        this.nickname = nickname;
        this.email = email;
    }
}
