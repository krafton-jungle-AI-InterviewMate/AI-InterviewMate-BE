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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class QuestionBoxesService {

    private final QuestionRepository questionRepository;
    private final QuestionBoxRepository questionBoxRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public QuestionBoxesService(QuestionRepository questionRepository, QuestionBoxRepository questionBoxRepository, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.questionRepository = questionRepository;
        this.questionBoxRepository = questionBoxRepository;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void createQuestion(Long questionBoxIdx, QuestionInfoDto questionInfoDto) {
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        //TODO: HG - Validator 써서 User 확인 추가
        Member member = questionBox.getMember();
        if (!Objects.equals(member.getIdx(), jwtTokenProvider.getUserInfo())) {
            throw new PrivateException(StatusCode.WRONG_REQUEST);
        }

        //TODO: HG - Validator 써서 공백 처리 필요
        if (questionInfoDto.getQuestionTitle() == null || questionInfoDto.getQuestionTitle().trim().isEmpty()) {
            throw new PrivateException(StatusCode.NOT_FOUND_QUESTION_TITLE);
        }

        questionRepository.save(questionInfoDto.ConvertToQuestionWithQuestionBox(questionBox));
        questionBox.setQuestionNum(questionBox.getQuestionNum() + 1);
    }

    public QuestionBoxInfoDto getQuestionBoxInfo(Long questionBoxIdx) {
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        Member member = questionBox.getMember();
        if (!Objects.equals(member.getIdx(), jwtTokenProvider.getUserInfo())) {
            throw new PrivateException(StatusCode.WRONG_REQUEST);
        }

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

        Member member = questionBox.getMember();
        if (!Objects.equals(member.getIdx(), jwtTokenProvider.getUserInfo())) {
            throw new PrivateException(StatusCode.WRONG_REQUEST);
        }

        questionRepository.deleteAllByQuestionBoxIdx(questionBoxIdx);

        questionBox.setQuestionNum(0);
    }

    public void updateQuestion(Long questionIdx, QuestionInfoDto questionInfoDto) {
        Question question = questionRepository.findByIdx(questionIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));

        //TODO: HG - Validator 써서 User 확인 추가
        QuestionBox questionBox = question.getQuestionBox();
        Member member = questionBox.getMember();
        if (!Objects.equals(member.getIdx(), jwtTokenProvider.getUserInfo())) {
            throw new PrivateException(StatusCode.WRONG_REQUEST);
        }

        //TODO: HG - Validator 써서 공백 처리 필요
        if (questionInfoDto.getQuestionTitle() == null || questionInfoDto.getQuestionTitle().trim().isEmpty()) {
            throw new PrivateException(StatusCode.NOT_FOUND_QUESTION_TITLE);
        }

        question.update(questionInfoDto);
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long questionIdx) {
        //TODO : JWT토근이 완성되면 넘에 값 예외처리 - 본인 데이터만 처리할 수 있게 처리 필요
        Question question = questionRepository.findByIdx(questionIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));

        questionRepository.deleteByIdx(questionIdx);

        QuestionBox questionBox = question.getQuestionBox();
        questionBox.setQuestionNum(questionBox.getQuestionNum() - 1);
    }

    @Transactional
    public void updateQuestionBoxInfo(Long questionBoxIdx, QuestionBoxInfoDto questionBoxInfoDto) {
        //TODO : JWT토근이 완성되면 넘에 값 예외처리 - 본인 데이터만 처리할 수 있게 처리 필요
        QuestionBox questionBox = questionBoxRepository.findByIdx(questionBoxIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTIONBOX));

        //TODO: HG - Validator 써서 공백 처리 필요
        String questionBoxName = questionBoxInfoDto.getQuestionBoxName();
        if (questionBoxName == null || questionBoxName.trim().isEmpty()) {
            throw new PrivateException(StatusCode.NOT_FOUND_QUESTION_BOX_TITLE);
        }

        questionBox.setBoxName(questionBoxName);
    }
}
