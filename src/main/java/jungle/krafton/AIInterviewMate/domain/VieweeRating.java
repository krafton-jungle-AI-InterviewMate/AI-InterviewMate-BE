package jungle.krafton.AIInterviewMate.domain;


import jungle.krafton.AIInterviewMate.dto.rating.RatingInterviewDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class VieweeRating {
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
    public VieweeRating(Long viewerIdx, Long vieweeIdx, RoomType roomType, Integer eyesRating, Integer attitudeRating, Integer answerRating) {
        this.viewerIdx = viewerIdx;
        this.vieweeIdx = vieweeIdx;
        this.roomType = roomType;
        this.eyesRating = eyesRating;
        this.attitudeRating = attitudeRating;
        this.answerRating = answerRating;
    }

    public VieweeRating(RatingInterviewDto ratingInterviewDto) {
        this.viewerIdx = ratingInterviewDto.getViewerIdx();
        this.answerRating = ratingInterviewDto.getAnswerRating();
        this.eyesRating = ratingInterviewDto.getEyesRating();
        this.attitudeRating = ratingInterviewDto.getAttitudeRating();
        this.vieweeIdx = 1L;
        this.roomType = viewerIdx == 79797979 ? RoomType.AI : RoomType.USER;
    }
}

