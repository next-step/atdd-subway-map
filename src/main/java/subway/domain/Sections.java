package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.Getter;
import subway.exception.IllegalSectionException;

@Getter
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "lineId", cascade = CascadeType.PERSIST)
    private List<Section> sectionList = new ArrayList<>();

    public void add(Section newSection) {
        if (unMatchUpStationAndDownStation(newSection)) {
            throw new IllegalSectionException();
        }

        if (containStationAlready(newSection.getDownStation())) {
            throw new IllegalSectionException();
        }

        sectionList.add(newSection);
    }

    public void delete(Station station) {
        if (sectionList.isEmpty()) {
            throw new IllegalSectionException();
        }

        if (!station.equals(lastSection().getDownStation())) {
            throw new IllegalSectionException();
        }

        sectionList.remove(sectionList.size() - 1);
    }

    private boolean unMatchUpStationAndDownStation(Section newSection) {
        if (sectionList.isEmpty()) {
            return false;
        }
        return !newSection.getUpStation().equals(lastSection().getDownStation());
    }

    private boolean containStationAlready(Station downStation) {
        List<Station> stations = sectionList.stream()
            .map(data -> List.of(data.getUpStation(), data.getDownStation()))
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());

        return stations.contains(downStation);
    }

    public int getTotalDistance() {
        return sectionList.stream().mapToInt(Section::getDistance).sum();
    }

    private Section lastSection() {
        return sectionList.get(sectionList.size() - 1);
    }
}
