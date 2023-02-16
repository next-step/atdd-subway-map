package subway.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public void removeLast() {
        sections.remove(sections.size() - 1);
    }

    public List<Station> getStations() {
        return sections.stream()
            .map(Section::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    public void validateUpStationForAdd(Station upStation) {
        if (!isDownwardEndPoint(upStation)) {
            throw new IllegalArgumentException("상행역은 노선의 하행종점이어야 합니다.");
        }
    }

    public void validateDownStationForAdd(Station downStation) {
        if (containsStation(downStation)) {
            throw new IllegalArgumentException("하행역은 노선에 등록된 역일 수 없습니다.");
        }
    }

    public void validateStationForRemove(Station station) {
        if (hasLessThenTwoSections()) {
            throw new IllegalArgumentException("노선에 두개 이상의 구간이 있어야 삭제가 가능합니다.");
        }
        if (!isDownwardEndPoint(station)) {
            throw new IllegalArgumentException("노선의 하행종점역만 삭제가 가능합니다.");
        }
    }

    private boolean isDownwardEndPoint(Station station) {
        Station downwardEndPoint = sections.get(sections.size() - 1).getDownStation();
        return downwardEndPoint.getId().equals(station.getId());
    }

    private boolean containsStation(Station station) {
        for (Station s : getStations()) {
            if (station.getId().equals(s.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLessThenTwoSections() {
        return sections.size() < 2;
    }
}
