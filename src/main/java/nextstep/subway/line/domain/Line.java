package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.line.exception.NoSectionException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade ={CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void addSection(Section section) {
        if (isAlreadyRegistered(section) || !isAppendableSection(section) ) {
            throw new InvalidSectionException("Input section is invalid");
        }
        this.sections.add(section);
        section.setLine(this);
    }

    private Boolean isLeftOneSection(){
        return (this.sections.size() == 1);
    }

    private Boolean isEmptySections() {
        return (this.sections.size() == 0);
    }

    private Boolean isAppendableSection(final Section section) {
        if (isEmptySections()) {
            return true;
        }
        return (getLastDownStationId() == section.getUpStation().getId());
    }

    private Boolean isAlreadyRegistered(final Section section) {
        return sections.stream()
                .anyMatch(s -> s.getUpStation().equals(section.getDownStation())
                        && s.getDownStation().equals(section.getDownStation()));
    }

    public void deleteStation(final Station station){
        final Section section = getLastSection();
        if(!section.getDownStation().equals(station) || isLeftOneSection()){
            throw new InvalidSectionException("Invalid Station Id" + station.getId());
        }
        sections.remove(section);
    }

    public List<StationResponse> getStations(){
        List<Station> sequentialStations = new ArrayList<>();
        for (Station station = firstStation(); station != null; station = nextStation(station)){
            sequentialStations.add(station);
        }
        return sequentialStations.stream()
                .map(StationResponse::of).collect(Collectors.toList());
    }

    private Station firstStation(){
        return sections.stream()
                .filter(section -> !matchAnyDownStation(section.getUpStation()))
                .map(section -> section.getUpStation())
                .findFirst()
                .orElse(null);
    }

    private Station nextStation(final Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .map(section -> section.getDownStation())
                .findFirst()
                .orElse(null);
    }

    public Section getLastSection(){
        return sections.stream()
                .filter(section -> !matchAnyUpStation(section.getDownStation()))
                .findFirst()
                .orElse(null);
    }

    private Boolean matchAnyUpStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.getUpStation().equals(station));
    }

    private Boolean matchAnyDownStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.getDownStation().equals(station));
    }

    public Long getLastUpStationId(){
        return getLastSection().getUpStation().getId();
    }

    public Long getLastDownStationId(){
        return getLastSection().getDownStation().getId();
    }

    public Integer getLineDistance() {
        return sections.stream().
                reduce(0, (TotalDistance, section) -> TotalDistance + section.getDistance(), Integer::sum);
    }
}
