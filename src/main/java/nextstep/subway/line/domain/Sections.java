package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

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

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public List<Station> getUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    public List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public Station getUpEndStation() {
        List<Station> upStations = getUpStations();
        List<Station> downStations = getDownStations();
        upStations.removeAll(downStations);
        return upStations.get(0);
    }

    public Station getDownEndStation() {
        List<Station> upStations = getUpStations();
        List<Station> downStations = getDownStations();
        downStations.removeAll(upStations);
        return downStations.get(0);
    }

    // 모든 상행역 + 하행 종점 = 모든 역
    public List<Station> getAllStations() {
        List<Station> upStations = getUpStations();
        upStations.add(getDownEndStation());
        return upStations;
    }

    public Section getLastSection() {
        Station downEndStation = getDownEndStation();
        return sections.stream()
                .filter(it -> it.getDownStation().equals(downEndStation))
                .findFirst().orElse(null);
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public boolean isAlreadyAddedStation(Station station) {
        return getAllStations().stream().anyMatch(station::equals);
    }

}
