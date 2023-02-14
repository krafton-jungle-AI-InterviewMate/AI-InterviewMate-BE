package jungle.krafton.AIInterviewMate.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ScriptSaveDto {
    private Long questionIdx;
    private String script;
}