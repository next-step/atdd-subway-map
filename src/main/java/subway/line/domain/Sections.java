package subway.line.domain;

import subway.station.domain.Station;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public List<Section> getSections() {
        return sections;
    }

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        var stations = sections.stream().map(section -> section.getUpStation()).collect(Collectors.toList());
        stations.add(getLastDownStation());

        return stations;
    }

    public List<Long> getStationIds() {
        return getStations().stream().map(Station::getId).collect(Collectors.toList());
    }

    public void addSection(Section section) {
        if(sections.isEmpty()) {
            sections.add(section);
            return;
        }

        //중복 체크
        isDuplication(section.getUpStation().getId(), section.getDownStation().getId());

        //하행 종점역인지 valid
        if(getLastDownStation().getId() != section.getUpStation().getId()) {
            throw new RuntimeException();
        }

        //add
        sections.add(section);
    }

    private Station getLastDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    private void isDuplication(Long upStationId, Long downStationId) {
        boolean isDuplication = getStations().stream().map(Station::getId).noneMatch(stationId -> stationId.equals(upStationId) || stationId.equals(downStationId));

        if(isDuplication) {
            throw new RuntimeException();
        }
    }

    public void deleteSection(Station station) {
        if(sections.isEmpty()) {
            throw new RuntimeException();
        }

        if(getLastDownStation().getId() != station.getId()) {
            throw new RuntimeException();
        }

        sections.remove(sections.size() - 1);
    }
}
