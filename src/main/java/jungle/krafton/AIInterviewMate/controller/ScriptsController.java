package jungle.krafton.AIInterviewMate.controller;

import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.ScriptsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scripts")
public class ScriptsController {
    private final ScriptsService scriptsService;

    public ScriptsController(ScriptsService scriptsService) {
        this.scriptsService = scriptsService;
    }

    @PostMapping("/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> saveScript(@RequestParam("idx") Long questionIdx, @PathVariable Long roomIdx, @RequestBody String script) {
        scriptsService.saveScript(questionIdx, roomIdx, script);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

}
