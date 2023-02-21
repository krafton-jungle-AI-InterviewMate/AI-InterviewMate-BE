package jungle.krafton.AIInterviewMate.domain;

import io.openvidu.java.client.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class OpenViduInfo {
    private final Map<String, Object> params;
    private final Session session;

    private OpenViduInfo(Map<String, Object> params, Session session) {
        this.params = params;
        this.session = session;
    }

    static public OpenViduInfo of(OpenVidu openVidu, InterviewRoom interviewRoom, Member member) {
        Map<String, Object> openViduParams = createParams(interviewRoom, member);
        Session session = createOpenViduSession(openVidu, openViduParams);

        return new OpenViduInfo(openViduParams, session);
    }

    static public Map<String, Object> createParams(InterviewRoom interviewRoom, Member member) {
        Map<String, Object> params = new HashMap<>();

        params.put("roomIdx", interviewRoom.getIdx());
        params.put("roomName", interviewRoom.getRoomName());
        params.put("memberNickname", member.getNickname());

        return params;
    }

    static private Session createOpenViduSession(OpenVidu openVidu, Map<String, Object> params) {
        SessionProperties properties = SessionProperties.fromJson(params).build();

        Session session = null;
        try {
            session = openVidu.createSession(properties);
        } catch (Exception e) {
            handleError(e);
        }

        return session;
    }

    static public void handleError(Exception e) {
        if (e instanceof OpenViduJavaClientException) {
            e.printStackTrace();
            throw new PrivateException(StatusCode.OPENVIDU_JAVA_SERVER_ERROR);
        } else if (e instanceof OpenViduHttpException) {
            e.printStackTrace();
            throw new PrivateException(StatusCode.OPENVIDU_SERVER_ERROR);
        } else if (e instanceof NullPointerException) {
            e.printStackTrace();
            throw new PrivateException(StatusCode.NOT_FOUND_SESSION);
        }
    }

    public String getConnectionToken() {
        ConnectionProperties connectionProperties = ConnectionProperties.fromJson(params).build();

        Connection connection = null;
        try {
            connection = session.createConnection(connectionProperties);
        } catch (Exception e) {
            handleError(e);
        }

        return connection.getToken();
    }

    public String getSessionId() {
        return this.session.getSessionId();
    }
}
