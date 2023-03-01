package jungle.krafton.AIInterviewMate.util;

import io.openvidu.java.client.*;
import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.dto.openvidu.OpenViduInfoDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class OpenViduCustomWrapper {
    private OpenVidu openVidu;
    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;
    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    @PostConstruct
    public void init() {
        this.openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    public OpenViduInfoDto createOpenViduInfoDto(InterviewRoom interviewRoom, Member member) {
        Map<String, Object> params = createParams(interviewRoom, member);
        Session session = null;
        String curSessionId = interviewRoom.getSessionId();

        try {
            if (isExistSessionId(curSessionId)) {
                this.openVidu.fetch();
                session = this.openVidu.getActiveSession(curSessionId);
            } else {
                session = createOpenViduSession(params);
            }
        } catch (NullPointerException e) {
            //이미 닫힌 방이므로 해당 예외는 무시처리
            System.out.println("createSession: 이미 닫힌 Session 입니다.");
        } catch (Exception e) {
            handleError(e);
        }

        return new OpenViduInfoDto(params, session);
    }

    public void closeSession(String sessionId) {
        try {
            this.openVidu.fetch();

            Session activeSession = this.openVidu.getActiveSession(sessionId);
            activeSession.close();
        } catch (NullPointerException e) {
            //이미 닫힌 방이므로 해당 예외는 무시처리
            System.out.println("closeSession: 이미 닫힌 Session 입니다.");
        } catch (Exception e) {
            handleError(e);
        }
    }

    private boolean isExistSessionId(String sessionId) {
        return sessionId != null;
    }

    public Map<String, Object> createParams(InterviewRoom interviewRoom, Member member) {
        Map<String, Object> params = new HashMap<>();

        params.put("roomIdx", interviewRoom.getIdx());
        params.put("roomName", interviewRoom.getRoomName());
        params.put("memberNickname", member.getNickname());

        return params;
    }

    private Session createOpenViduSession(Map<String, Object> params) throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();

        return this.openVidu.createSession(properties);
    }

    public void handleError(Exception e) {
        e.printStackTrace();

        if (e instanceof OpenViduJavaClientException) {
            throw new PrivateException(StatusCode.OPENVIDU_JAVA_SERVER_ERROR);
        } else if (e instanceof OpenViduHttpException) {
            throw new PrivateException(StatusCode.OPENVIDU_SERVER_ERROR);
        } else if (e instanceof PrivateException) {
            throw (PrivateException) e;
        } else {
            throw new PrivateException(StatusCode.INTERNAL_SERVER_ERROR_PLZ_CHECK);
        }
    }
}
