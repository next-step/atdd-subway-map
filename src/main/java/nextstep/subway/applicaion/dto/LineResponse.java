package nextstep.subway.applicaion.dto;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final SectionsResponse sectionsResponse;

    public LineResponse(Long id, String name, String color, List<SectionResponse> sectionsResponse) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionsResponse = new SectionsResponse(sectionsResponse);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public SectionsResponse getStations() {
        return sectionsResponse;
    }
}
