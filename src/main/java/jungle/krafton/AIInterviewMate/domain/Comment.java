package jungle.krafton.AIInterviewMate.domain;

import jungle.krafton.AIInterviewMate.dto.rating.CommentsRequestDto;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewRoom_idx", nullable = false)
    private InterviewRoom interviewRoom;

    @Column(nullable = false)
    private Long viewerIdx;

    @Column(nullable = false)
    private Long vieweeIdx;

    @Column(nullable = false)
    private String questionTitle;

    @Column(nullable = false, length = 1000)
    private String comment;


    public Comment(InterviewRoom interviewRoom, CommentsRequestDto commentsRequestDto) {
        this.interviewRoom = interviewRoom;
        this.vieweeIdx = 1L; //RoomIdx 를 보고 면접자를 알아야 함
        this.viewerIdx = commentsRequestDto.getViewerIdx();
        this.questionTitle = commentsRequestDto.getQuestionTitle();
        this.comment = commentsRequestDto.getComment();
    }
}
