package subway.section.domain;

import org.springframework.util.CollectionUtils;
import subway.section.exception.DownEndStationRegisteredOnLineException;
import subway.section.exception.DownStationAlreadyExistsException;
import subway.section.exception.DownStationMustBeUpStationException;
import subway.section.exception.OnlyOneSectionException;
import subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Section> sectionList = new ArrayList<>();

    public Section addSection(Section section) {

        if (!sectionList.isEmpty()) {
            if (notEqualsLastStation(section.getUpStation())) {
                throw new DownStationAlreadyExistsException();
            }


            if (containStations(section.getDownStation())) {
                throw new DownStationMustBeUpStationException();
            }

        }

        sectionList.add(section);

        return section;
    }

    public void removeStation(Station station) {

        if (isOnlyOneSection())
            throw new OnlyOneSectionException();

        if (isNotDownEndStation(station))
            throw new DownEndStationRegisteredOnLineException();

        Section deleteSection = getDeleteSection(station);
        sectionList.remove(deleteSection);
    }

    public List<Station> getStationList() {
        return sectionList.stream().flatMap(
                section -> Stream.of(section.getUpStation(), section.getDownStation())
        ).distinct().collect(Collectors.toList());
    }

    private boolean containStations(Station station) {
        return getStationList().contains(station);
    }

    private boolean notEqualsLastStation(Station section) {
        Station lastStation = CollectionUtils.lastElement(getStationList());
        return !section.equals(lastStation);
    }

    private boolean isNotDownEndStation(Station section) {
        Station lastStation = CollectionUtils.lastElement(getStationList());
        return !section.equals(lastStation);
    }

    private boolean isOnlyOneSection() {
        return sectionList.size() == 1;
    }

    private Section getDeleteSection(Station station) {
        return sectionList.stream()
                .filter(section -> station.equals(section.getDownStation()))
                .findFirst()
                .get();
    }

}