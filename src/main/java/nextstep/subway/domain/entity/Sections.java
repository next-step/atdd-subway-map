package nextstep.subway.domain.entity;

import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.exception.BadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        if (anyNonMatchSection(section -> section.equalsDownStation(newSection.getUpStation()))) {
            throw new BadRequestException("upStation required connected line downStation");
        }

        if (anyMatchSection(section -> section.equalsUpStation(newSection.getUpStation()))) {
            throw new BadRequestException("upStation is already connected");
        }

        if (anyMatchSection(section -> section.existAnyStation(newSection.getDownStation()))) {
            throw new BadRequestException("downStation is already connected");
        }

        sections.add(newSection);
    }

    public List<Station> getStations() {
        return sections.stream()
                .map(section -> new Station[]{section.getUpStation(), section.getDownStation()})
                .flatMap(Arrays::stream)
                .distinct()
                .sorted(Comparator.comparing(Station::getId))
                .collect(Collectors.toUnmodifiableList());
    }

    public void delete(Station station) {
        if (!hasGreaterThenMinSize()) {
            throw new BadRequestException("section can not delete");
        }

        Station lastStation = sections.get(getLastIndex()).getDownStation();

        if (!station.equals(lastStation)) {
            throw new BadRequestException("section can not delete");
        }

        sections.remove(getLastIndex());
    }

    private boolean anyNonMatchSection(Predicate<Section> condition) {
        return !anyMatchSection(condition);
    }

    private boolean anyMatchSection(Predicate<Section> condition) {
        return sections.stream().anyMatch(condition);
    }

    private boolean hasGreaterThenMinSize() {
        return sections.size() > MIN_SIZE;
    }

    private int getLastIndex() {
        return sections.size() - 1;
    }

}
