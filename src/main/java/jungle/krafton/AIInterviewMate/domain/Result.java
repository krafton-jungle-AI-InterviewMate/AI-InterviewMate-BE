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
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewRoom_idx", nullable = false)
    private InterviewRoom interviewRoom;

    @Column()
    private String videoUrl;

    @Column(nullable = false, length = 1000)
    private String eyeTimeline;

    @Column(nullable = false, length = 1000)
    private String attitudeTimeline;

    @Column(length = 1000)
    private String questionTimeline;

    @Column(length = 1000)
    private String comment;
}
