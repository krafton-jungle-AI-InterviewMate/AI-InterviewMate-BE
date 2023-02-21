package jungle.krafton.AIInterviewMate.dto.interview;

import io.openvidu.java.client.*;
import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class OpenViduInfoDto {
    private final Map<String, Object> params;
    private final Session session;

    private OpenViduInfoDto(Map<String, Object> params, Session session) {
        this.params = params;
        this.session = session;
    }

    static public OpenViduInfoDto of(OpenVidu openVidu, InterviewRoom interviewRoom, Member member) {
        Map<String, Object> openViduParams = createParams(interviewRoom, member);
        Session session = createOpenViduSession(openVidu, openViduParams);

        return new OpenViduInfoDto(openViduParams, session);
    }

    static private Session createOpenViduSession(OpenVidu openVidu, Map<String, Object> params) {
        SessionProperties properties = SessionProperties.fromJson(params).build();

        Session session;
        try {
            session = openVidu.createSession(properties);
        } catch (OpenViduJavaClientException e) {
            e.printStackTrace();
            throw new PrivateException(StatusCode.OPENVIDU_JAVA_SERVER_ERROR);
        } catch (OpenViduHttpException e) {
            e.printStackTrace();
            throw new PrivateException(StatusCode.OPENVIDU_SERVER_ERROR);
        }

        if (session == null) {
            throw new PrivateException(StatusCode.OPENVIDU_JAVA_SERVER_ERROR);
        }

        return session;
    }

    static private Map<String, Object> createParams(InterviewRoom interviewRoom, Member member) {
        Map<String, Object> params = new HashMap<>();

        params.put("roomIdx", interviewRoom.getIdx());
        params.put("roomName", interviewRoom.getRoomName());
        params.put("memberNickname", member.getNickname());

        return params;
    }

    public String getSessionId() {
        return this.session.getSessionId();
    }

    public String getConnectionToken() {
        ConnectionProperties connectionProperties = ConnectionProperties.fromJson(params).build();

        Connection connection;
        try {
            connection = session.createConnection(connectionProperties);
        } catch (OpenViduJavaClientException e) {
            e.printStackTrace();
            throw new PrivateException(StatusCode.OPENVIDU_JAVA_SERVER_ERROR);
        } catch (OpenViduHttpException e) {
            e.printStackTrace();
            throw new PrivateException(StatusCode.OPENVIDU_SERVER_ERROR);
        }

        return connection.getToken();
    }
}
