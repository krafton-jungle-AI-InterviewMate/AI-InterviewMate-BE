package jungle.krafton.AIInterviewMate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {
    OK(HttpStatus.OK, "0", "정상"),

    SIGNUP_NICKNAME_FORM_ERROR(HttpStatus.BAD_REQUEST, "100", "nickname 형식을 맞춰주세요"),
    NULL_INPUT_CHAT_REQUEST(HttpStatus.BAD_REQUEST, "101", "필수 입력항목중 미입력 항목이 존재합니다."),
    NULL_QUERY_STRING(HttpStatus.BAD_REQUEST, "102", "쿼리스트링이 존재하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "103", "해당 유저가 존재하지 않습니다."),

    NOT_FOUND_JWT_TOKEN(HttpStatus.NOT_FOUND, "110", "JWT 이 존재하지 않습니다. 다시 확인해주세요."),
    LOGIN_WRONG_SIGNATURE_JWT_TOKEN(HttpStatus.BAD_REQUEST, "111", "잘못된 JWT 서명입니다."),
    LOGIN_EXPIRED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "112", "만료된 JWT 토큰입니다."),
    LOGIN_NOT_SUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "113", "지원되지 않는 JWT 토큰입니다."),
    LOGIN_WRONG_FORM_JWT_TOKEN(HttpStatus.BAD_REQUEST, "114", "JWT 토큰이 잘못되었습니다."),
    WRONG_SOCIAL_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "115", "잘못된 소셜 로그인 형식입니다."),
    NOT_FOUND_REFRESH_TOKEN_COOKIE(HttpStatus.NOT_FOUND, "116", "REFRESH_TOKEN 쿠키를 찾을 수 없습니다."),
    NOT_VALIDATED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "117", "잘못된 REFRESH 토큰입니다."),
    NOT_MATCHED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "118", "잘못된 REFRESH_TOKEN 서명입니다."),

    PAGING_ERROR(HttpStatus.BAD_REQUEST, "120", "페이지 요청에는 모든 요소가 필요합니다"),
    PAGING_NUM_ERROR(HttpStatus.BAD_REQUEST, "121", "페이지는 1부터 시작됩니다"),

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "130", "해당 회원을 찾을 수 없습니다"),
    NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST, "131", "방을 찾을 수 없습니다."),
    NOT_FOUND_QUESTION(HttpStatus.BAD_REQUEST, "132", "질문을 찾을 수 없습니다."),
    NOT_FOUND_QUESTIONBOX(HttpStatus.BAD_REQUEST, "133", "꾸러미를 찾을 수 없습니다."),

    NOT_UPDATE_EXIT_ROOM(HttpStatus.BAD_REQUEST, "140", "종료된 방의 상태를 수정할 수 없습니다."),

    NOT_MATCH_REDIRECT_URL(HttpStatus.BAD_REQUEST, "300", "리다이렉트 주소가 일치하지 않습니다."),

    NULL_INPUT_ERROR(HttpStatus.NOT_FOUND, "990", "Null 값이 들어왔습니다"),
    NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT(HttpStatus.INTERNAL_SERVER_ERROR, "998", "Security Context에 인증 정보가 없습니다."),
    INTERNAL_SERVER_ERROR_PLZ_CHECK(HttpStatus.INTERNAL_SERVER_ERROR, "999", "알수없는 서버 내부 에러 발생. 서버 담당자에게 알려주세요.");

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String statusMsg;

    StatusCode(HttpStatus httpStatus, String statusCode, String statusMsg) {
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
}
