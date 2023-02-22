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

    @Transactional
    public void createQuestion(Long questionBoxIdx, QuestionInfoDto questionInfoDto) {
        //TODO : JWT토근이 완성되면 넘에 값 예외처리 - 본인 데이터만 수정할 수 있게 수정 필요
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        //TODO: HG - Validator 써서 공백 처리 필요
        if (questionInfoDto.getQuestionTitle() == null || questionInfoDto.getQuestionTitle().trim().isEmpty()) {
            throw new PrivateException(StatusCode.NOT_FOUND_QUESTION_TITLE);
        }
        
        questionRepository.save(questionInfoDto.ConvertToQuestionWithQuestionBox(questionBox));
        questionBox.setQuestionNum(questionBox.getQuestionNum() + 1);
    }

    public QuestionBoxInfoDto getQuestionBoxInfo(Long questionBoxIdx) {
        //TODO : JWT토근이 완성되면 넘에 값 예외처리 - 본인 데이터만 볼 수 있게 처리 필요
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        List<Question> questions = questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);

        List<QuestionInfoDto> questionInfoDtos = new ArrayList<>();

        for (Question question : questions) {
            questionInfoDtos.add(QuestionInfoDto.of(question));
        }

        return QuestionBoxInfoDto.of(questionBox, questionInfoDtos);
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


    }
}
