package subway.line;

import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sectionList = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public static Sections from(List<Section> sectionList) {
        return new Sections(sectionList);
    }

    public List<Station> startStations() {
        return this.sectionList.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    public Station lastStation() {
        return this.sectionList.get(sectionList.size() - 1).getDownStation();
    }

    public void add(Section section) {
        this.sectionList.add(section);
    }

    public boolean isSameLastStationAndStartStation(Section station) {
        return station.isSameUpStation(lastStation());
    }

    public boolean anyMatchStation(Section section) {
        return this.sectionList.stream()
                .anyMatch(s -> s.anyMatchUpStationOrDownStation(section));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections = (Sections) o;
        return Objects.equals(sectionList, sections.sectionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionList);
    }

    public Section delete(Station station) {
        Section lastSection = this.sectionList.stream()
                .filter(section -> section.isSameDownStation(station))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("삭제할 역을 찾지 못하였습니다."));
        this.sectionList.remove(lastSection);
        return lastSection;
    }
}
