package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Script;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.InterviewRoomRepository;
import jungle.krafton.AIInterviewMate.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ScriptsService {
    private final ScriptRepository scriptRepository;
    private final InterviewRoomRepository interviewRoomRepository;

    @Autowired
    public ScriptsService(ScriptRepository scriptRepository, InterviewRoomRepository interviewRoomRepository) {
        this.scriptRepository = scriptRepository;
        this.interviewRoomRepository = interviewRoomRepository;
    }


    public void saveScript(Long questionIdx, Long roomIdx, HashMap<String, Object> script) {
        InterviewRoom interviewRoom = interviewRoomRepository.findById(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));
        scriptRepository.save(convertScript(questionIdx, interviewRoom, String.valueOf(script.get("script"))));
    }

    private Script convertScript(Long questionIdx, InterviewRoom interviewRoom, String script) {
        return Script.builder()
                .interviewRoom(interviewRoom)
                .memberIdx(interviewRoom.getMember().getIdx())
                .questionIdx(questionIdx)
                .script(script)
                .build();
    }
}
