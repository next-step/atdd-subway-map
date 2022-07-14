package nextstep.subway.applicaion.dto;

import java.util.List;

public class SectionsResponse {
    private final List<SectionResponse> sections;

    public SectionsResponse(List<SectionResponse> sections) {
        this.sections = sections;
    }

    public List<SectionResponse> getStations() {
        return List.copyOf(sections);
    }
}
