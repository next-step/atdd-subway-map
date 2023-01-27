package subway.section.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.entity.Section;
import subway.station.entity.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
        return values.add(section);
    }

    @Override
    public Section remove(int index) {
        return values.remove(index);
    }

    public List<Station> stations() {
        if (values.isEmpty()) {
            return new ArrayList<>();
        }

        return createStations();
    }

    private List<Station> createStations() {
        if (values.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> stations = values.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        stations.add(getLastStation().getDownStation());
        return stations;
    }

    private Section getLastStation() {
        return values.get(values.size() - 1);
    }
}
