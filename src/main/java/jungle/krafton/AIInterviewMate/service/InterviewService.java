package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.dto.interview.InterviewQuestionDto;
import jungle.krafton.AIInterviewMate.dto.interview.InterviewRoomInfoDto;
import jungle.krafton.AIInterviewMate.repository.InterviewRoomRepository;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterviewService {
    private final InterviewRoomRepository interviewRoomRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public InterviewService(InterviewRoomRepository interviewRoomRepository, QuestionRepository questionRepository, MemberRepository memberRepository) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
    }

    public InterviewRoomInfoDto getRoomInfo(long roomIdx) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx);
        String memberNickname = interviewRoom.getMember().getNickname();
        long questionBoxIdx = interviewRoom.getRoomQuestionBoxIdx();
        List<Question> questions = questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);
        List<InterviewQuestionDto> interviewQuestions = new ArrayList<>();
        for (Question question : questions) {
            interviewQuestions.add(new InterviewQuestionDto(question));
        }

        return new InterviewRoomInfoDto(interviewRoom, interviewQuestions, memberNickname);
    }
}
