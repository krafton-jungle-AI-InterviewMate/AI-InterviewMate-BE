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
    @JoinColumn(name = "interviewRoom_idx", nullable = false, unique = true)
    private InterviewRoom interviewRoom;

    @Column(length = 1000)
    private String videoUrl;

    @Column(columnDefinition = "LONGTEXT")
    private String eyeTimeline;

    @Column(columnDefinition = "LONGTEXT")
    private String attitudeTimeline;

    @Column(columnDefinition = "LONGTEXT")
    private String questionTimeline;

    @Column(columnDefinition = "LONGTEXT")
    private String memo;

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
