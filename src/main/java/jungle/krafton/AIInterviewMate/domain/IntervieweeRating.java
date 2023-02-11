package jungle.krafton.AIInterviewMate.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class IntervieweeRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @Column(nullable = false)
    private Long viewerIdx;

    @Column(nullable = false)
    private Long vieweeIdx;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private Integer eyesRating;

    @Column(nullable = false)
    private Integer attitudeRating;

    @Column(nullable = false)
    private Integer answerRating;


    @Builder
    public IntervieweeRating(Long viewerIdx, Long vieweeIdx, RoomType roomType, Integer eyesRating, Integer attitudeRating, Integer answerRating) {
        this.viewerIdx = viewerIdx;
        this.vieweeIdx = vieweeIdx;
        this.roomType = roomType;
        this.eyesRating = eyesRating;
        this.attitudeRating = attitudeRating;
        this.answerRating = answerRating;
    }
}

