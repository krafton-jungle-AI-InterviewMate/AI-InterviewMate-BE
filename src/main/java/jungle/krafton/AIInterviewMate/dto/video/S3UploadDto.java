package jungle.krafton.AIInterviewMate.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class S3UploadDto {
    private String uploadId;
    private String fileName;
}
