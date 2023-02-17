package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.domain.QuestionBox;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionBoxRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionBoxesService {

    private final QuestionRepository questionRepository;
    private final QuestionBoxRepository questionBoxRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public QuestionBoxesService(QuestionRepository questionRepository, QuestionBoxRepository questionBoxRepository, MemberRepository memberRepository) {
        this.questionRepository = questionRepository;
        this.questionBoxRepository = questionBoxRepository;
        this.memberRepository = memberRepository;
    }

    public List<Question> createQuestionList(Long questionBoxIdx) {              //TODO : JWT토근이 완성되면 넘에 값 예외처리
        return questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);
    }

    public List<QuestionBox> createQuestionBox(String email) {              //TODO : JWT토근이 완성되면 넘에 값 예외처리
        Member memberIdx = memberRepository.findByEmail(email).orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));
        return questionBoxRepository.findAllByMember(memberIdx);
    }
}
