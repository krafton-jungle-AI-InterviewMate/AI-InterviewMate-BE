package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.domain.QuestionBox;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionBoxInfoDto;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionInfoDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionBoxRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import jungle.krafton.AIInterviewMate.validator.Validator;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final Validator validator;

    @Autowired
    public QuestionBoxesService(QuestionRepository questionRepository, QuestionBoxRepository questionBoxRepository, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, Validator validator) {
        this.questionRepository = questionRepository;
        this.questionBoxRepository = questionBoxRepository;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.validator = validator;
    }

    @Transactional
    public void createQuestion(Long questionBoxIdx, QuestionInfoDto questionInfoDto) {
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        validator.validateMember(questionBox.getMember(), jwtTokenProvider);

        validator.validateName(questionInfoDto.getQuestionTitle());

        questionRepository.save(questionInfoDto.ConvertToQuestionWithQuestionBox(questionBox));
        questionBox.setQuestionNum(questionBox.getQuestionNum() + 1);
    }

    public QuestionBoxInfoDto getQuestionBoxInfo(Long questionBoxIdx) {
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        validator.validateMember(questionBox.getMember(), jwtTokenProvider);

        List<Question> questions = questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);

        List<QuestionInfoDto> questionInfoDtos = new ArrayList<>();

        for (Question question : questions) {
            questionInfoDtos.add(QuestionInfoDto.of(question));
        }

        return QuestionBoxInfoDto.of(questionBox, questionInfoDtos);
    }


    public List<QuestionBoxInfoDto> getQuestionBoxes() {
        Long memberIdx = jwtTokenProvider.getUserInfo();

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
    public void clearQuestionBox(Long questionBoxIdx) {
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        validator.validateMember(questionBox.getMember(), jwtTokenProvider);

        questionRepository.deleteAllByQuestionBoxIdx(questionBoxIdx);

        questionBox.setQuestionNum(0);
    }

    public void updateQuestion(Long questionIdx, QuestionInfoDto questionInfoDto) {
        Question question = questionRepository.findByIdx(questionIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));

        QuestionBox questionBox = question.getQuestionBox();

        validator.validateMember(questionBox.getMember(), jwtTokenProvider);

        validator.validateName(questionInfoDto.getQuestionTitle());

        question.update(questionInfoDto);
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long questionIdx) {
        Question question = questionRepository.findByIdx(questionIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));

        QuestionBox questionBox = question.getQuestionBox();

        validator.validateMember(questionBox.getMember(), jwtTokenProvider);

        questionRepository.deleteByIdx(questionIdx);

        questionBox.setQuestionNum(questionBox.getQuestionNum() - 1);
    }

    @Transactional
    public void updateQuestionBoxName(Long questionBoxIdx, QuestionBoxInfoDto questionBoxInfoDto) {
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));
        
        validator.validateMember(questionBox.getMember(), jwtTokenProvider);

        String questionBoxName = questionBoxInfoDto.getQuestionBoxName();

        validator.validateName(questionBoxName);

        questionBox.setBoxName(questionBoxName);
    }
}
