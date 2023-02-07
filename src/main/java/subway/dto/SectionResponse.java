package subway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Optional;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

@JsonInclude(Include.NON_NULL)
public class SectionResponse {

    private Long id;
    private Integer distance;
    private Long upStationId;
    private Long downStationId;

    public SectionResponse(
            final Long id,
            final Distance distance,
            final Optional<Station> upStation,
            final Optional<Station> downStation
    ) {
        this.id = id;
        this.distance = distance.getValue();
        this.upStationId = upStation.map(Station::getId).orElse(null);
        this.downStationId = downStation.map(Station::getId).orElse(null);
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
