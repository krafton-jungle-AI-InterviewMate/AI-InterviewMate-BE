package jungle.krafton.AIInterviewMate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewRoom_idx", nullable = false)
    private InterviewRoom interviewRoom;

    @Column(nullable = false)
    private Long questionIdx;

    @Column(columnDefinition = "LONGTEXT")
    private String script;

    public void setScript(String script) {
        this.script = script;
    }
}
