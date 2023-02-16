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

    @OneToMany(mappedBy = "lineId", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public void add(Section newSection) {
        validateAdd(newSection);
        sectionList.add(newSection);
    }

    private void validateAdd(Section newSection) {
        unMatchStationThrowException(newSection);
        alreadyContainStationThrowException(newSection.getDownStation());
    }

    public void delete(Station station) {
        validateDelete(station);
        sectionList.remove(sectionList.size() - 1);
    }

    private void validateDelete(Station station) {
        if (sectionList.isEmpty()) {
            throw new IllegalSectionException();
        }

        if (!station.equals(lastSection().getDownStation())) {
            throw new IllegalSectionException();
        }
    }

    private void unMatchStationThrowException(Section newSection) {
        if (sectionList.isEmpty()) {
            return;
        }
        if (!newSection.getUpStation().equals(lastSection().getDownStation())) {
            throw new IllegalSectionException();
        }
    }

    private void alreadyContainStationThrowException(Station downStation) {
        boolean contains = sectionList.stream()
            .map(data -> List.of(data.getUpStation(), data.getDownStation()))
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList())
            .contains(downStation);

        if (contains) {
            throw new IllegalSectionException();
        }
    }

    public int getTotalDistance() {
        return sectionList.stream().mapToInt(Section::getDistance).sum();
    }

    private Section lastSection() {
        return sectionList.get(sectionList.size() - 1);
    }
}
