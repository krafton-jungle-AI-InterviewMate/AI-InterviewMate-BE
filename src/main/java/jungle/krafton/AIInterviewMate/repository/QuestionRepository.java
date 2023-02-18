package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByQuestionBoxIdx(Long questionBoxIdx);

    void deleteAllByQuestionBoxIdx(Long idx);

    void deleteByIdx(Long idx);

    Optional<Question> findByIdx(Long idx);

}
