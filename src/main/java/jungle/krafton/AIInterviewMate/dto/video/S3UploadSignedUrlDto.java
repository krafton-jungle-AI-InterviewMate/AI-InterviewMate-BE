package jungle.krafton.AIInterviewMate.dto.video;

import lombok.Getter;

@Getter
public class S3UploadSignedUrlDto {
    private String fileName;
    private String uploadId;
    private Integer partNumber;
}
