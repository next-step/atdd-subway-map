package nextstep.subway.domain;

import nextstep.subway.exception.AlreadyRegisteredStationInLineException;
import nextstep.subway.exception.DownStationNotMatchException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addFirstSection(Section section) {
        if (!sections.isEmpty()) {
            throw new RuntimeException("첫 구간 추가시에만 가능");
        }
        sections.add(section);
    }

    public void addSection(Section section) {
        validateDownStation(section.getUpStation());
        validateAlreadyRegisteredStation(section.getDownStation());
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    private void validateDownStation(Station upStation) {
        if (!isAddableSection(upStation)) {
            throw new DownStationNotMatchException(upStation.getName());
        }
    }

    private void validateAlreadyRegisteredStation(Station dowStation) {
        if (getAllStations().contains(dowStation)) {
            throw new AlreadyRegisteredStationInLineException(dowStation.getName());
        }
    }

    private boolean isAddableSection(Station upStation) {
        Station lastDownStation = getLastDownStation();
        return lastDownStation.equals(upStation);
    }

    private Station getLastDownStation() {
        int lastIndex = sections.size() - 1;
        Section section = sections.get(lastIndex);
        return section.getDownStation();
    }

    private List<Station> getAllStations() {
        List<Station> stations = sections.stream()
                .map(it -> it.getUpStation())
                .collect(Collectors.toList());
        stations.add(getLastDownStation());

        return stations;
    }

}
