package nextstep.subway.applicaion.dto;

public class SectionResponse {

    private Long sectionId;

    private SectionResponse(Long sectionId) {
        this.sectionId = sectionId;
    }

    public static SectionResponse of(Long sectionId) {
        return new SectionResponse(sectionId);
    }

    public Long getSectionId() {
        return sectionId;
    }
}
