package subway.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Sections;

public class SectionResponse {
    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;
    private final int distance;

    public SectionResponse(Long upStationId, String upStationName, Long downStationId, String downStationName, int distance) {
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public static List<SectionResponse> createSectionResponse(Sections sections) {
        return sections.getSections().stream().map(
            section -> new SectionResponse(section.getUpStation().getId(),
                                           section.getUpStation().getName(),
                                           section.getDownStation().getId(),
                                           section.getDownStation().getName(),
                                           section.getDistance())
        ).collect(Collectors.toList());
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }
}
