package jungle.krafton.AIInterviewMate.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ViewerRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @Column(nullable = false)
    private Long viewerIdx;

    @Column(nullable = false)
    private Long vieweeIdx;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Long roomIdx;

    @Builder
    public ViewerRating(Long viewerIdx, Long vieweeIdx, Integer rating, Long roomIdx) {
        this.viewerIdx = viewerIdx;
        this.vieweeIdx = vieweeIdx;
        this.rating = rating;
        this.roomIdx = roomIdx;
    }
}
