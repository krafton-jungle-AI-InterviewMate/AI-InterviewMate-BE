package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByQuestionBoxIdx(Long questionBoxIdx);
    
    void deleteAllByQuestionBoxIdx(Long idx);

    Question findByIdx(Long idx);
}
