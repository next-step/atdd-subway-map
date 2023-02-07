package subway.dto;

public class SectionCreateResponse {
    private Long lineId;
    private SectionResponse section;

    public SectionCreateResponse(Long id, SectionResponse sectionResponse) {
        this.lineId = id;
        this.section = sectionResponse;
    }

    public Long getLineId() {
        return lineId;
    }

    public SectionResponse getSection() {
        return section;
    }
}
