package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.VieweeRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VieweeRatingRepository extends JpaRepository<VieweeRating, Long> {
    Optional<VieweeRating> findByRoomIdx(Long roomIdx);

    List<VieweeRating> findAllByRoomIdx(Long roomIdx);

    VieweeRating findByRoomIdxAndViewerIdx(Long roomIdx, Long vieweeIdx);
}
