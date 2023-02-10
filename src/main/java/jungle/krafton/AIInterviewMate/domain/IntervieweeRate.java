package jungle.krafton.AIInterviewMate.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class IntervieweeRate {
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
    private Integer eyesRate;

    @Column(nullable = false)
    private Integer attitudeRate;

    @Column(nullable = false)
    private Integer answerRate;


    @Builder
    public IntervieweeRate(Long idx, Long viewerIdx, Long vieweeIdx, RoomType roomType, Integer eyesRate, Integer attitudeRate, Integer answerRate) {
        this.idx = idx;
        this.viewerIdx = viewerIdx;
        this.vieweeIdx = vieweeIdx;
        this.roomType = roomType;
        this.eyesRate = eyesRate;
        this.attitudeRate = attitudeRate;
        this.answerRate = answerRate;
    }
}

