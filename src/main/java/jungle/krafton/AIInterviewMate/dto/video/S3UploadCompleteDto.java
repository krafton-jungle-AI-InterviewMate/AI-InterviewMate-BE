package jungle.krafton.AIInterviewMate.dto.video;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class S3UploadCompleteDto {
    private List<S3UploadPartsDetailDto> parts;
    private String fileName;
    private String uploadId;
    private Long roomIdx;
}
