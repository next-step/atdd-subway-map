package subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import subway.exception.CannotDeleteSectionException;
import subway.exception.ErrorMessage;
import subway.line.section.domain.Section;
import subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    private int getLastIndexOfSections() {
        return sections.size() - 1;
    }

    public void removeLastSection() {
        sections.remove(getLastIndexOfSections());
    }

    public void addSection(Station upStation, Station downStation, Line line, Long distance) {
        sections.add(new Section(upStation, downStation, line, distance));
    }

    public Long getLastSectionDistance() {
        return sections.get(getLastIndexOfSections()).getDistance();
    }

    public List<Station> getAllStations() {
        List<Station> stations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(sections.get(getLastIndexOfSections()).getDownStation());

        return stations;
    }

    public Station getDownStationOfLastSection() {
        return sections.get(getLastIndexOfSections()).getDownStation();
    }

    public boolean hasStation(Station downStation) {
        return getAllStations().contains(downStation);
    }

    public void validSizeOfSection() {
        if (sections.size() == 1) {
            throw new CannotDeleteSectionException(ErrorMessage.CANNOT_DELETE_LINE_CONSIST_OF_ONE_SECTION);
        }
    }
}
