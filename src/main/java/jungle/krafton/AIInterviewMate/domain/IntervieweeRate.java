package jungle.krafton.AIInterviewMate.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "IntervieweeRate")
public class IntervieweeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @Column(nullable = false)
    private Long viewer_idx;

    @Column(nullable = false)
    private Long viewee_idx;

    @Enumerated
    @Column(nullable = false)
    private RoomType room_type;

    @Column(nullable = false)
    private Integer eyes_rate;

    @Column(nullable = false)
    private Integer attitude_rate;

    @Column(nullable = false)
    private Integer answer_rate;


    @Builder
    public IntervieweeRate(Long idx, Long viewer_idx, Long viewee_idx, String room_type, Integer eyes_rate, Integer attitude_rate, Integer answer_rate) {
        this.idx = idx;
        this.viewer_idx = viewer_idx;
        this.viewee_idx = viewee_idx;
        this.room_type = room_type;
        this.eyes_rate = eyes_rate;
        this.attitude_rate = attitude_rate;
        this.answer_rate = answer_rate;
    }

}

