package jungle.krafton.AIInterviewMate.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "InterviewerRating")
        public class InterviewerRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @Column(nullable = false)
    private Long viewer_idx;

    @Column(nullable = false)
    private Long viewee_idx;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Long room_idx;

    @Builder
    public InterviewerRating(Long idx, Long viewer_idx, Long viewee_idx, Integer rating, Long room_idx) {
        this.idx = idx;
        this.viewer_idx = viewer_idx;
        this.viewee_idx = viewee_idx;
        this.rating = rating;
        this.room_idx = room_idx;
    }
}
