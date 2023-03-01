package jungle.krafton.AIInterviewMate;

import jungle.krafton.AIInterviewMate.dto.result.ResultTimelineDto;

import java.util.Comparator;

public class TimelineComparator implements Comparator<ResultTimelineDto> {
    @Override
    public int compare(ResultTimelineDto o1, ResultTimelineDto o2) {
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}
