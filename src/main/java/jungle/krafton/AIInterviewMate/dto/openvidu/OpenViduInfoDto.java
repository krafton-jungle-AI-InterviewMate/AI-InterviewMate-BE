package jungle.krafton.AIInterviewMate.dto.openvidu;

import io.openvidu.java.client.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;

import java.util.Map;

public class OpenViduInfoDto {
    private final Map<String, Object> params;
    private final Session session;

    public OpenViduInfoDto(Map<String, Object> params, Session session) {
        this.params = params;
        this.session = session;
    }

    public String getConnectionToken() {
        ConnectionProperties connectionProperties = ConnectionProperties.fromJson(params).build();

        try {
            Connection connection = session.createConnection(connectionProperties);

            return connection.getToken();
        } catch (Exception e) {
            e.printStackTrace();

            if (e instanceof OpenViduJavaClientException) {
                throw new PrivateException(StatusCode.OPENVIDU_JAVA_SERVER_ERROR);
            } else if (e instanceof OpenViduHttpException) {
                throw new PrivateException(StatusCode.OPENVIDU_SERVER_ERROR);
            } else if (e instanceof NullPointerException) {
                throw new PrivateException(StatusCode.NOT_FOUND_SESSION);
            } else {
                throw new PrivateException(StatusCode.INTERNAL_SERVER_ERROR_PLZ_CHECK);
            }
        }
    }

    public String getSessionId() {
        return this.session.getSessionId();
    }
}
