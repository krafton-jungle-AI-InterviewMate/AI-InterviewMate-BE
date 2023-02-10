package jungle.krafton.AIInterviewMate.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrivateResponseBody {
    private String statusCode;
    private String statusMsg;
    private Object data;

    public PrivateResponseBody(StatusCode statusCode) {
        this.statusCode = statusCode.getStatusCode();
        this.statusMsg = statusCode.getStatusMsg();
    }

    public PrivateResponseBody(StatusCode statusCode, Object data) {
        this.statusCode = statusCode.getStatusCode();
        this.statusMsg = statusCode.getStatusMsg();
        this.data = data;
    }
}
