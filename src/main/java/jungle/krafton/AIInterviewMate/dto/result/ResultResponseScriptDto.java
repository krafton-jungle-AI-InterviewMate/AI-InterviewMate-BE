package jungle.krafton.AIInterviewMate.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ResultResponseScriptDto {
    private String questionTitle;
    private String script;
}
