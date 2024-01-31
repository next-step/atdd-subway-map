package subway.line;

import subway.line.section.SectionResponse;

import java.util.List;

public class LineSectionResponse {
    private Long lineId;
    private String name;
    private Long upStationId;
    private Long downStationId;
    private List<SectionResponse> sections;

    public LineSectionResponse(Long lineId, String name, Long upStationId, Long downStationId, List<SectionResponse> sections) {
        this.lineId = lineId;
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.sections = sections;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
