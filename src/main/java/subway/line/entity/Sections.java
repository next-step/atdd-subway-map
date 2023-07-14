package subway.line.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import subway.station.entity.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {
    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Section> sections = new ArrayList<>();

    private Sections(Section section) {
        this.sections.add(section);
    }

    public static Sections init(Section section) {
        return new Sections(section);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(getFirstStation());
        sections.stream()
                .forEach(section -> stations.add(section.getDownStation()));
        return stations;
    }

    public void addSection(Section section) {
        validateEnrollment(section);
        sections.add(section);
    }

    public void remove(Station station) {
        validateDeletion(station);
        sections.remove(getLastSection());
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public Station getFirstStation() {
        return sections.get(0).getUpStation();
    }

    public Station getLastStation() {
        return getLastSection().getDownStation();
    }

    private void validateEnrollment(Section section) {
        if (!section.getUpStation().equalsId(this.getLastStation())) {
            throw new IllegalArgumentException("새로운 구간의 상행 역이 해당 노선에 등록되어있는 하행 종착역이 아님.");
        }

        if (this.getStations().contains(section.getDownStation())) {
            throw new IllegalArgumentException("새로운 구간의 하행역이 해당 노선에 등록되어있는 역임.");
        }
    }

    private void validateDeletion(Station station) {
        if (!this.getLastStation().equalsId(station)) {
            throw new IllegalArgumentException(String.format("노선의 마지막 역이 아닙니다. 역id:%s", station.getId()));
        }

        if (this.sections.size() == 1) {
            throw new IllegalArgumentException("상행 종점역과 하행 종점역만 존재합니다.");
        }
    }
}
