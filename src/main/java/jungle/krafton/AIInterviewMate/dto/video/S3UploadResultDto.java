package jungle.krafton.AIInterviewMate.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class S3UploadResultDto {
    private String name;
    private String url;
    private Long size;
}
