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
    public Member(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Member update(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
