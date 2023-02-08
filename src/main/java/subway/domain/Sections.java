package subway.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.PERSIST;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        sections.forEach(section -> {
            stations.add(section.getUpStation());
            if (sections.indexOf(section) == sections.size() - 1) {
                stations.add(section.getDownStation());
            }
        });
        return stations;
    }

    private Station getHeadStation() {
        return sections.get(0).getUpStation();
    }

    private Station getTailStation() {
        List<Station> stations = getStations();
        return stations.get(stations.size() - 1);
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getStations();
        Station tailStation = getTailStation();

        Station upStation = section.getUpStation();
        if (!tailStation.equals(upStation)) {
            throw new IllegalArgumentException(
                    String.format("새로운 구간의 상행역은 현재 노선의 하행 종점역이어야 합니다. (하행 종점역: %d)", tailStation.getId())
            );
        }

        Station downStation = section.getDownStation();
        if (stations.contains(downStation)) {
            throw new IllegalArgumentException(
                    String.format("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다. (요청값: %d)", downStation.getId())
            );
        }

        sections.add(section);
    }

    public void remove(Station station) {
        if (isRemainedOneSection()) {
            throw new IllegalStateException("지하철노선이 하나의 구간만 존재할 경우, 역을 삭제할 수 없습니다.");
        }

        Station tailStation = getTailStation();
        if (!tailStation.equals(station)) {
            throw new IllegalArgumentException(
                    String.format("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있습니다. 즉, 마지막 구간만 제거할 수 있습니다. (하행 종점역: %d)", tailStation.getId())
            );
        }

        sections.remove(sections.size() - 1);
    }

    private boolean isRemainedOneSection() {
        return sections.size() == 1;
    }
}
