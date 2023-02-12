package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.VieweeRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VieweeRatingRepository extends JpaRepository<VieweeRating, Long> {
    VieweeRating findByRoomIdx(Long roomIdx);
}
