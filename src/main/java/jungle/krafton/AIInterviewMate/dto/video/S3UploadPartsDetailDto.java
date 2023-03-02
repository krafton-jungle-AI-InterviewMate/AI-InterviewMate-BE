package jungle.krafton.AIInterviewMate.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class S3UploadPartsDetailDto {
    private Integer partNumber;
    private String awsETag;
}
