package jungle.krafton.AIInterviewMate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class InterviewRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoomType roomType;

    @Column(nullable = false)
    private String roomName;

    @Column()
    private String roomPassword;

    @Column(nullable = false)
    private boolean isPrivate;

    @Column()
    private Integer roomTime;

    @Column(name = "question_num")
    private Integer roomQuestionNum;

    @Column(nullable = false, unique = true, name = "question_box_idx")
    private Long roomQuestionBoxIdx;

    @Column()
    private Integer roomPeopleNum;

    @Column(name = "interviewer_1_idx")
    private Long roomInterviewer1Idx;

    @Column(name = "interviewer_2_idx")
    private Long roomInterviewer2Idx;

    @Column(name = "interviewer_3_idx")
    private Long roomInterviewer3Idx;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoomStatus roomStatus;

    @Builder
    public InterviewRoom(Member member, RoomType roomType, String roomName, String roomPassword, boolean isPrivate, Integer roomTime, Integer roomQuestionNum, Long roomQuestionBoxIdx, Integer roomPeopleNum) {
        this.member = member;
        this.roomType = roomType;
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.isPrivate = isPrivate;
        this.roomTime = roomTime;
        this.roomQuestionNum = roomQuestionNum;
        this.roomQuestionBoxIdx = roomQuestionBoxIdx;
        this.roomPeopleNum = roomPeopleNum;
        this.roomStatus = RoomStatus.CREATE;
    }
}
