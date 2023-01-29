package subway.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import subway.domain.Line;
import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class LineSectionResponse {

    private Long id;
    private String name;
    private String color;
    @JsonProperty("sections")
    private List<SectionResponse> sectionResponses;

    private LineSectionResponse() {}

    public LineSectionResponse(final Long id, final String name, final String color, final List<SectionResponse> sectionResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionResponses = sectionResponses;
    }

    public static LineSectionResponse createResponse(final Line line) {
        final List<SectionResponse> sectionResponse = convertToSectionResponse(line.getSections().getSections());
        return new LineSectionResponse(line.getId(), line.getName(), line.getColor(), sectionResponse);
    }

    private static List<SectionResponse> convertToSectionResponse(final List<Section> sections) {
        return sections.stream()
                .map(SectionResponse::from)
                .collect(Collectors.toList());
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

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }
}
