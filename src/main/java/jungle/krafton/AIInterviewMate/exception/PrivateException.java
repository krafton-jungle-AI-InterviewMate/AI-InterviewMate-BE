package jungle.krafton.AIInterviewMate.exception;

import lombok.Getter;

@Getter
public class PrivateException extends RuntimeException {
    private final StatusCode statusCode;

    public PrivateException(StatusCode statusCode) {
        super(statusCode.getStatusMsg());
        this.statusCode = statusCode;
    }
}
