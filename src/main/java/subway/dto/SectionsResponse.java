package subway.dto;

import java.util.List;

public class SectionsResponse {
    private Long lineId;
    private List<SectionResponse> sections;

    public SectionsResponse(Long lineId, List<SectionResponse> sections) {
        this.lineId = lineId;
        this.sections = sections;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
