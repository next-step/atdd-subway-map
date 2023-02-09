package subway.domain.section;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.global.error.ErrorCode;
import subway.global.error.exception.ValidateException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of(List<Section> sections) {
        return new Sections(sections);
    }

    public void belongTo(Line line) {
        sections.forEach(section -> section.belongTo(line));
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream().
                map(Section::getUpStation).
                collect(Collectors.toList());
        stations.add(sections.get(sections.size()-1).getDownStation());
        return stations;
    }

    public void addSection(Section section) {
        validateLineLastStationIsEqualToNewSectionUpStation(section);
        validateExistStation(section);
        sections.add(section);
    }

    private void validateExistStation(Section section) {
        List<Station> stations = getStations();
        if (stations.contains(section.getDownStation())) {
            throw new ValidateException(ErrorCode.ALREADY_EXIST_STATION);
        }
    }

    private void validateLineLastStationIsEqualToNewSectionUpStation(Section section) {
        Station lastStation = getLastStation();
        if (!lastStation.equals(section.getUpStation())) {
            throw new ValidateException(ErrorCode.SECTION_MUST_BE_ADDED_END_OF_LINE);
        }
    }

    private Station getLastStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }
}
