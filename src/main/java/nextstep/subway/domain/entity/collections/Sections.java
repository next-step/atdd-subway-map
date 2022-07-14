package nextstep.subway.domain.entity.collections;

import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.exception.BadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        return sections.stream()
                .map(section -> new Station[]{section.getUpStation(), section.getDownStation()})
                .flatMap(Arrays::stream)
                .distinct()
                .sorted(Comparator.comparing(Station::getId))
                .collect(Collectors.toUnmodifiableList());
    }

    public void deleteStation(Station station) {
        if (!hasGreaterThenMinSize()) {
            throw new BadRequestException("section can not delete");
        }

        Station lastStation = sections.get(getLastIndex()).getDownStation();

        if (!station.equals(lastStation)) {
            throw new BadRequestException("section can not delete");
        }

        sections.remove(getLastIndex());
    }

    private boolean hasGreaterThenMinSize() {
        return this.sections.size() > MIN_SIZE;
    }

    private int getLastIndex() {
        return sections.size() - 1;
    }

}
