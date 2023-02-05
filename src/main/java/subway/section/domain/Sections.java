package subway.section.domain;

import subway.common.util.CollectionUtil;
import subway.section.exception.DownStationAlreadyExistsException;
import subway.section.exception.DownStationMustBeUpStationException;
import subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Section> stationList = new ArrayList<>();

    public Section addSection(Section section) {

        if (!stationList.isEmpty()) {
            if (notEqualsLastStation(section.getUpStation()))
                throw new DownStationAlreadyExistsException();

            if (containStations(section.getDownStation()))
                throw new DownStationMustBeUpStationException();
        }

        stationList.add(section);

        return section;
    }

    public List<Station> getStationList() {
        LinkedHashSet stationSet = new LinkedHashSet();
        stationList.stream().forEach((section) -> {
            stationSet.add(section.getUpStation());
            stationSet.add(section.getDownStation());
        });
        return new ArrayList<>(stationSet);
    }

    private boolean notEqaulsDownStation(Station station) {
        return !containStations(station);
    }

    private boolean containStations(Station section) {
        return getStationList().contains(section);
    }

    private boolean notEqualsLastStation(Station section) {
        Station lastStation = CollectionUtil.lastItemOfList(getStationList());
        return !section.equals(lastStation);
    }

    private boolean equalsLastStation(Station section) {
        Station lastStation = CollectionUtil.lastItemOfList(getStationList());
        return section.equals(lastStation);
    }

}