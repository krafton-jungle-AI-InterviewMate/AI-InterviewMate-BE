package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.domain.QuestionBox;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionBoxInfoDto;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionInfoDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionBoxRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public void createQuestion(Long questionBoxIdx, QuestionKeywordDto questionKeywordDto) {
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx).orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));
        questionRepository.save(convertQuestion(questionBox, questionKeywordDto));
    }

    public List<Question> createQuestionList(Long questionBoxIdx) {              //TODO : JWT토근이 완성되면 넘에 값 예외처리
        return questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);
    }


    public List<QuestionBoxInfoDto> getQuestionBoxes(Long memberIdx) {
        //TODO : JWT토근이 완성되면 넘에 값 예외처리 - 본인 데이터만 볼 수 있게 처리 필요
        Member member = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));

        List<QuestionBox> questionBoxList = questionBoxRepository.findAllByMember(member);

        List<QuestionBoxInfoDto> questionBoxInfoDtos = new ArrayList<>();

        for (QuestionBox questionBox : questionBoxList) {
            questionBoxInfoDtos.add(QuestionBoxInfoDto.of(questionBox));
        }

        return questionBoxInfoDtos;
    }

    @Transactional
    public void clearQuestionBox(Long questionBoxIdx) {              //TODO : JWT토근이 완성되면 넘에 값 예외처리
        questionRepository.deleteAllByQuestionBoxIdx(questionBoxIdx);
    }

    public void updateKeyword(Long questionIdx, QuestionKeywordDto questionKeywordDto) {              //TODO : JWT토근이 완성되면 넘에 값 예외처리
        Question question = questionRepository.findByIdx(questionIdx).orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));
        question.setKeyword(questionKeywordDto);
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long questionIdx) {              //TODO : JWT토근이 완성되면 넘에 값 예외처리
        questionRepository.deleteByIdx(questionIdx);
    }

    public QuestionBoxListDto convertQuestionBox(QuestionBox questionBox) {
        return QuestionBoxListDto.builder()
                .idx(questionBox.getIdx())
                .boxName(questionBox.getBoxName())
                .questionNum(questionBox.getQuestionNum())
                .build();
    }

    public Question convertQuestion(QuestionBox questionBox, QuestionKeywordDto questionKeywordDto) {
        return Question.builder()
                .questionBox(questionBox)
                .questionTitle(questionKeywordDto.getQuestionTitle())
                .keyword1(questionKeywordDto.getKeyword1())
                .keyword2(questionKeywordDto.getKeyword2())
                .keyword3(questionKeywordDto.getKeyword3())
                .keyword4(questionKeywordDto.getKeyword4())
                .keyword5(questionKeywordDto.getKeyword5())
                .build();
    }
}
