package jungle.krafton.AIInterviewMate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Builder
    public Member(Long idx, String nickname, String email) {
        this.idx = idx;
        this.nickname = nickname;
        this.email = email;
    }
}
