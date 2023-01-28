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

        Long lastSectionDownStationId = this.lastSection().getDownStation().getId();
        Long newSectionUpStationId = newSection.getUpStation().getId();
        Long newSectionDownStationId = newSection.getDownStation().getId();

        if (lastSectionDownStationId != newSectionUpStationId) {
            throw new IllegalArgumentException(String.format("마지막 구간의 하행역과 추가하려는 구간의 상행역이 다릅니다. 하행역 id:%d, 상행역 id:%d"
                    , lastSectionDownStationId
                    , newSectionUpStationId));
        }

        boolean isSavedSectionStation = allStations()
                .stream()
                .anyMatch(station -> station.getId() == newSectionDownStationId);

        if (isSavedSectionStation) {
            throw new IllegalArgumentException("이미 노선에 등록된 역 입니다. id:" + newSectionDownStationId);
        }
    }

    @Override
    public Section remove(int index) {
        return values.remove(index);
    }

    public List<Station> allStations() {
        if (values.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> upStations = upStations();
        upStations.add(lastSection().getDownStation());
        return upStations;
    }

    private List<Station> upStations() {
        return values.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private Section lastSection() {
        return values.get(values.size() - 1);
    }
}
