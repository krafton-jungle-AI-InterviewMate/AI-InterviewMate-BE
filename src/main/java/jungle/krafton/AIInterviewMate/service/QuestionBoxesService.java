package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionBoxesService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionBoxesService(QuestionRepository questionBoxRepository) {
        this.questionRepository = questionBoxRepository;
    }

    public List<Question> createQuestionList(Long questionBoxIdx) {              //TODO : JWT토근이 완성되면 넘에 값 예외처리
        return questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);
    }
}
