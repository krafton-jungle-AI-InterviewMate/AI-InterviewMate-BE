package jungle.krafton.AIInterviewMate.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Global error controller

/*
    @GetMapping("/anonypost/board/{boardPostId}")
    public ResponseEntity<PrivateResponseBody> getBoardDetails(@PathVariable(value = "boardPostId") Long boardPostId, @RequestParam(value = "id" , required = false) String memberId){
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, boardService.getBoardDetails(boardPostId,memberId)), HttpStatus.OK);
    }
    ※ new PrivateResponseBody(StatusCode.OK, boardService.getBoardDetails(boardPostId,memberId)) ==> 해당 부분만 수정해서 사용하면 됩니다.
 */

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {PrivateException.class})
    public ResponseEntity<Object> handleApiRequestException(PrivateException ex) {
        String errCode = ex.getStatusCode().getStatusCode();
        String errMSG = ex.getStatusCode().getStatusMsg();
        PrivateResponseBody privateResponseBody = new PrivateResponseBody();
        privateResponseBody.setStatusCode(errCode);
        privateResponseBody.setStatusMsg(errMSG);

        System.out.println("ERR :" + errCode + " , " + errMSG);  //Log용

        return new ResponseEntity<>(
                privateResponseBody,
                ex.getStatusCode().getHttpStatus()
        );
    }
}
