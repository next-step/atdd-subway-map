package subway.domain;

import subway.exception.CannotAppendableDownStationException;
import subway.exception.CannotAppendableUpStationException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.PERSIST;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        Station headStation = getHeadStation();
        stations.add(0, headStation);

        return stations;
    }

    private Station getHeadStation() {
        return sections.get(0).getUpStation();
    }

    private Station getTailStation() {
        List<Station> stations = getStations();
        return stations.get(stations.size() - 1);
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getStations();

        Station tailStation = getTailStation();
        Station upStation = section.getUpStation();

        if (!tailStation.equals(upStation)) {
            throw new CannotAppendableUpStationException(upStation.getId());
        }

        Station downStation = section.getDownStation();
        if (stations.contains(downStation)) {
            throw new CannotAppendableDownStationException(downStation.getId());
        }

        sections.add(section);
    }
}
