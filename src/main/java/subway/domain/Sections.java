package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import subway.exception.LineNotConnectableException;
import subway.exception.LineNotDisConnectableException;

@Embeddable
public class Sections {

    @OneToOne
    Station startStation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Section> sections;

    public Sections(Station startStation, List<Section> sections) {
        this.startStation = startStation;
        this.sections = sections;
    }

    public Sections() {}

    public static Sections of(Station startStation, List<Section> sections) {
        return new Sections(startStation, new ArrayList<>(List.copyOf(sections)));
    }

    public void connect(Station destinationStation, Section section) {
        if (isNotDownEndStation(destinationStation)) {
            throw new LineNotConnectableException(String.format(
                "새로운 구간은 노선의 하행종착역을 상행역으로 설정해야 합니다: %d <> %d",
                getDownEndStation().getId(), destinationStation.getId()));
        }
        if (containsStation(section.getStartStation())) {
            throw new LineNotConnectableException("신규 구간의 하행역이 기존 노선에 포함되어 있습니다: " + section.getStartStation().getId());
        }
        sections.add(section);
    }

    public Station getDownEndStation() {
        return sections.get(sections.size() - 1).getStartStation();
    }

    private boolean isNotDownEndStation(Station destinationStation) {
        return !getDownEndStation().equals(destinationStation);
    }

    private boolean containsStation(Station station) {
        return startStation.equals(station) || sections.stream()
            .map(Section::getStartStation)
            .anyMatch(s -> Objects.equals(s, station));
    }

    public Long getDistance() {
        return sections.stream()
            .mapToLong(Section::getDistance)
            .sum();
    }

    public void update(Sections sections) {
        this.startStation = sections.startStation;
        this.sections.clear();
        this.sections.addAll(sections.sections);
    }

    public void disconnectStation(Station deleteStation) {
        if (!contains(deleteStation)) {
            throw new LineNotDisConnectableException("입력된 ID에 해당하는 역이 노선에 포함되지 않습니다: " + deleteStation.getId());
        }
        if (!Objects.equals(getDownEndStation(), deleteStation)) {
            throw new LineNotDisConnectableException(String.format(
                "노선의 하행종착역만 삭제 가능합니다: %d <> %d",
                getDownEndStation().getId(), deleteStation.getId()));
        }
        if (sections.size() == 1) {
            throw new LineNotDisConnectableException("단일 구간 노선은 구간 제거가 불가합니다.");
        }
    }

    private boolean contains(Station station) {
        return Objects.equals(startStation, station) || sections.stream()
            .map(Section::getStartStation)
            .anyMatch(s -> Objects.equals(s, station));
    }
}
