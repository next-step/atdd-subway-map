package subway.section.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.global.error.code.ErrorCode;
import subway.global.error.exception.InvalidLineSectionException;
import subway.station.entity.Station;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    @ElementCollection
    @CollectionTable(name = "section", joinColumns = @JoinColumn(name = "line_id"))
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        int lastIndex = sections.size() - 1;
        Station lastStation = sections.get(lastIndex).getDownStation();

        if (!lastStation.equals(section.getUpStation())) {
            throw new InvalidLineSectionException(ErrorCode.INVALID_UP_STATION);
        }

        if (getAllStations().contains(section.getDownStation())) {
            throw new InvalidLineSectionException(ErrorCode.ALREADY_REGISTERED_STATION);
        }

        sections.add(section);
    }

    public List<Station> getAllStations() {
        List<Station> stations = this.sections
                .stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        int lastIndex = sections.size() - 1;
        stations.add(sections.get(lastIndex).getDownStation());

        return stations;
    }

}
