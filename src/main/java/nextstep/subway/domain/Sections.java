package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Line line, Station upStation, Station downStation, int distance) {
        if (sections.size() < MIN_SECTIONS_SIZE) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isNotEquals(upStation)) {
            throw new IllegalArgumentException();
        }

        if (isDownStationExist(downStation)) {
            throw new IllegalArgumentException();
        }

        sections.add(new Section(line, upStation, downStation, distance));
    }

    public void remove(Station station) {
        if (sections.size() <= MIN_SECTIONS_SIZE) {
            throw new IllegalArgumentException();
        }

        if (isNotLastStation(station)) {
            throw new IllegalArgumentException();
        }

        sections.remove(sections.size() - 1);
    }

    private boolean isNotLastStation(Station station) {
        List<Station> stations = getStations();
        return !stations.get(stations.size() - 1).equals(station);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(getLastStation());
        return stations;
    }

    private boolean isNotEquals(Station upStation) {
        return !getLastStation().equals(upStation);
    }

    private boolean isDownStationExist(Station downStation) {
        return getStations().contains(downStation);
    }

    private Station getLastStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }
}
