package subway.dto;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long distance;

    public SectionResponse(Long id, StationResponse upStation, StationResponse downStation, Long distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId()
                , StationResponse.of(section.getUpStation())
                , StationResponse.of(section.getDownStation())
                , section.getDistance());
    }

    public static List<SectionResponse> asList(List<Section> sections) {
        return sections.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
