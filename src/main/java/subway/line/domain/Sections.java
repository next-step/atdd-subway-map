package subway.line.domain;

import subway.line.exception.CustomException;
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

    public List<Long> getUpStationIds() {
        return sections.stream().map(section -> section.getUpStation().getId()).collect(Collectors.toList());
    }

    public List<Long> getDownStationIds() {
        return sections.stream().map(section -> section.getDownStation().getId()).collect(Collectors.toList());
    }

    public void addSection(Section section) {
        if(sections.isEmpty()) {
            sections.add(section);
            return;
        }

        if(isLastStation(section.getDownStation())) {
            throw new CustomException(CustomException.CAN_CREATE_ONLY_LAST_SECTION_MSG);
        }

        if(isStationExist(section.getDownStation())) {
            throw new CustomException(CustomException.ALREADY_CREATED_SECTION_MSG);
        }

        sections.add(section);
    }

    private boolean isLastStation(Station station) {
        return getLastDownStation().getId() == station.getId();
    }

    private Station getLastDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    private boolean isStationExist(Station station) {
        return getStations().stream()
                .map(Station::getId)
                .anyMatch(stationId -> stationId.equals(station.getId()));
    }

    public void deleteSection(Station station) {
        if(sections.isEmpty()) {
            throw new CustomException(CustomException.EMPTY_SECTIONS_IN_LINE);
        }

        if(!isStationExist(station)) {
            throw new CustomException(CustomException.EMPTY_SECTIONS_IN_LINE);
        }

        if(!isLastStation(station)) {
            throw new CustomException(CustomException.CAN_DELETE_ONLY_LAST_SECTION_MSG);
        }

        sections.remove(sections.size() - 1);
    }
}
