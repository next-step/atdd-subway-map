package nextstep.subway.domain;

import nextstep.subway.exception.UnmatchedLastStationAndNewUpStationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

@Embeddable
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> value = new ArrayList<>();

    public void addSection(Section section) {
        if (value.isEmpty()) {
            this.value.add(section);
            return;
        }

        matchLastStationAndNewUpStation(section.getUpStation());

        this.value.add(section);
    }

    private void matchLastStationAndNewUpStation(Station upStation) {
        Station lastStation = value.get(lastSection()).getDownStation();

        if (!lastStation.equals(upStation)) {
            throw new UnmatchedLastStationAndNewUpStationException("기존 노선의 종점역과 신규 노선의 상행역이 일치하지 않습니다.");
        }
    }

    private int lastSection() {
        return value.size() - 1;
    }

    public List<Station> allStations() {
        List<Station> stations = new ArrayList<>();

        if (value.isEmpty()) {
            return unmodifiableList(stations);
        }

        stations.add(value.get(0).getUpStation());

        value.forEach(section -> {
            stations.add(section.getDownStation());
        });

        return unmodifiableList(stations);
    }

}
