package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Long> {
    List<InterviewRoom> findAllByMemberIdxOrderByCreatedAtDesc(Long memberIdx);

    Optional<InterviewRoom> findByIdx(Long roomIdx);

    //Sort by [타입 - 상태 - 시간]
    List<InterviewRoom> findAllByRoomStatusNotOrderByRoomTypeDescRoomStatusAscCreatedAtDesc(RoomStatus status1);

    //Page
    Page<InterviewRoom> findAllByMemberIdx(Long memberIdx, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM interview_room " +
                    "WHERE member_id = :memberIdx and idx >= :stanIdx " +
                    "LIMIT 20")
    List<InterviewRoom> findAllByMemberIdxGraterThanStanIdx(long memberIdx, int stanIdx);
}
