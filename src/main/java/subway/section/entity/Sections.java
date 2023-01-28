package subway.section.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import subway.station.entity.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Sections extends AbstractList<Section> {

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Section> values = new ArrayList<>();

    public static Sections from(List<Section> values) {
        return new Sections(values);
    }

    @Override
    public Section get(int index) {
        return values.get(index);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean add(Section section) {
        validateForAdd(section);
        return values.add(section);
    }

    private void validateForAdd(Section newSection) {
        // 이미 저장 되어있거나 구간이 없으면 항상 통과하므로 검증할 필요가 없다.
        if (values.isEmpty() || values.contains(newSection)) {
            return;
        }

        long lastSectionDownStationId = this.lastSection().getDownStation().getId();
        Long newSectionUpStationId = newSection.getUpStation().getId();

        if (lastSectionDownStationId != newSectionUpStationId) {
            throw new IllegalArgumentException();
        }

        boolean isSavedSectionStation = this.stations()
                .stream()
                .anyMatch(station -> station.getId() == newSectionUpStationId); // 하행은 위에서 체크했으므로 상행만 체크한다.

        if (isSavedSectionStation) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Section remove(int index) {
        return values.remove(index);
    }

    public List<Station> stations() {
        if (values.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> stations = values.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        stations.add(lastSection().getDownStation());
        return stations;
    }

    private Section lastSection() {
        return values.get(values.size() - 1);
    }
}
