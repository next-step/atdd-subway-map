package subway.dto;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public class SectionResponse {

    private Long id;
    private Integer distance;
    private Long upStationId;
    private Long downStationId;

    public SectionResponse(
            final Long id,
            final Distance distance,
            final Station upStation,
            final Station downStation
    ) {
        this.id = id;
        this.distance = distance.getValue();
        this.upStationId = upStation.getId();
        this.downStationId = downStation.getId();
    }

    public static SectionResponse by(final Section section) {
        return new SectionResponse(
                section.getId(),
                section.getDistance(),
                section.getUpStation(),
                section.getDownStation()
        );
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
