package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections); // 불변으로 하기 위해 new ArrayList를 사용
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(sections.get(sections.size() - 1).getDownStation());

        return stations;
    }

    private List<Long> getStationIds() {
        return getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    public Long getDownStationId() {
        return getStationIds().get(getStationIds().size() - 1);
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public boolean isRegisteredStation(Long stationId) {
        return getStationIds().contains(stationId);
    }

    public boolean canDelete() {
        return sections.size() > 1;
    }

}
