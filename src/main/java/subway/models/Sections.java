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
        if (!sections.isEmpty()) {
            validateUpStationForAdd(section.getUpStation());
            validateDownStationForAdd(section.getDownStation());
        }
        sections.add(section);
    }

    public void removeLast(Station station) {
        validateStationForRemove(station);
        int lastIndex = sections.size() - 1;
        sections.remove(lastIndex);
    }

    public List<Station> getStations() {
        return sections.stream()
            .map(Section::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    public void validateUpStationForAdd(Station upStation) {
        if (isNotDownwardEndPoint(upStation)) {
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
        if (isNotDownwardEndPoint(station)) {
            throw new IllegalArgumentException("노선의 하행종점역만 삭제가 가능합니다.");
        }
    }

    private boolean isNotDownwardEndPoint(Station station) {
        int lastIndex = sections.size() - 1;
        Station downwardEndPoint = sections.get(lastIndex).getDownStation();
        return !downwardEndPoint.equals(station);
    }

    private boolean containsStation(Station station) {
        return getStations().stream().anyMatch(s -> s.equals(station));
    }

    private boolean hasLessThenTwoSections() {
        int limitOfSectionCountForDelete = 2;
        return sections.size() < limitOfSectionCountForDelete;
    }
}
