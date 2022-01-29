package nextstep.subway.domain.object;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values;

    public Sections() {
        this.values = new ArrayList<>();
    }

    public void add(Section section) {
        this.values.add(section);
    }

    public Section last() {
        if (this.values.isEmpty()) {
            return null;
        }
        return this.values.get(this.values.size() - 1);
    }

    public boolean equalsLastDownStation(Station upStation) {
        return this.last().getDownStation().equals(upStation);
    }

    public boolean checkDuplicatedDownStation(Station downStation) {
        boolean duplicatedDownStationYn = this.values.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet())
                .contains(downStation);
        boolean duplicatedUpStationYn = this.values.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toSet())
                .contains(downStation);
        return duplicatedDownStationYn || duplicatedUpStationYn;
    }

    public List<Station> getAllStations() {
        List<Station> stations = this.values.stream()
                .sorted(Comparator.comparing(Section::getId))
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(last().getDownStation());
        return stations;
    }

    public int size() {
        return this.values.size();
    }

    public void validateAddSection(Station upStation, Station downStation) {
        if (values.size() == 0) {
            return;
        }

        if (!equalsLastDownStation(upStation)) {
            throw new InvalidParameterException();
        }

        if (checkDuplicatedDownStation(downStation)) {
            throw new InvalidParameterException();
        }
    }

    public void validateRemoval(Station station) {
        if (this.values.size() <= 1) { throw new InvalidParameterException(); }

        if (!last().getDownStation().equals(station)) {
            throw new InvalidParameterException();
        }
    }

    public void removeLastSection() {
        this.values.remove(last());
    }
}
