package jungle.krafton.AIInterviewMate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @JsonIgnore
    @Id
    @Column()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String refreshToken;

    @Builder
    public Member(String nickname, String email, UserRole role, AuthProvider authProvider) {
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
    }

    public Member update(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
