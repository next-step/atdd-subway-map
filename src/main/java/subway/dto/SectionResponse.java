package subway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.domain.Section;
import subway.domain.Station;

@JsonInclude(Include.NON_NULL)
public class SectionResponse {

    private final Long id;
    private final String name;
    private Long upStationId;
    private Long downStationId;

    public SectionResponse(
            final Station station,
            final Optional<Station> upStation,
            final Optional<Station> downStation
    ) {
        this.id = station.getId();
        this.name = station.getName();
        this.upStationId = upStation.map(Station::getId).orElse(null);
        this.downStationId = downStation.map(Station::getId).orElse(null);
    }

    public static List<SectionResponse> by(final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionResponse(
                        section.getStation(),
                        section.getUpStation(),
                        section.getDownStation())
                )
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
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
}
