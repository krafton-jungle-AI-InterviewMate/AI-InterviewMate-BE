package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.VieweeRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VieweeRatingRepository extends JpaRepository<VieweeRating, Long> {
    VieweeRating findByRoomIdx(Long roomIdx);

    List<VieweeRating> findAllByRoomIdx(Long roomIdx);
}
