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
        Map<String, Object> params = createParams(interviewRoom, member);
        Session session = null;
        String curSessionId = interviewRoom.getSessionId();

        try {
            if (isExistSessionId(curSessionId)) {
                openVidu.fetch();
                session = openVidu.getActiveSession(curSessionId);
            } else {
                session = createOpenViduSession(openVidu, params);
            }
        } catch (Exception e) {
            handleError(e);
        }

        return new OpenViduInfo(params, session);
    }

    private static boolean isExistSessionId(String sessionId) {
        return sessionId != null;
    }

    static public Map<String, Object> createParams(InterviewRoom interviewRoom, Member member) {
        Map<String, Object> params = new HashMap<>();

        params.put("roomIdx", interviewRoom.getIdx());
        params.put("roomName", interviewRoom.getRoomName());
        params.put("memberNickname", member.getNickname());

        return params;
    }

    static private Session createOpenViduSession(OpenVidu openVidu, Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();

        return openVidu.createSession(properties);
    }

    static public void handleError(Exception e) {
        e.printStackTrace();

        if (e instanceof OpenViduJavaClientException) {
            throw new PrivateException(StatusCode.OPENVIDU_JAVA_SERVER_ERROR);
        } else if (e instanceof OpenViduHttpException) {
            throw new PrivateException(StatusCode.OPENVIDU_SERVER_ERROR);
        } else if (e instanceof NullPointerException) {
            throw new PrivateException(StatusCode.NOT_FOUND_SESSION);
        } else if (e instanceof PrivateException) {
            throw (PrivateException) e;
        } else {
            throw new PrivateException(StatusCode.INTERNAL_SERVER_ERROR_PLZ_CHECK);
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
