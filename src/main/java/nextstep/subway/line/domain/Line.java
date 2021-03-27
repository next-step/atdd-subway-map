package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.common.exception.InvalidRequestException;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
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

    public void addSection(SectionRequest sectionRequest, Station inputUpStation, Station inputDownStation) {
        final Section newSection = new Section(this, inputUpStation, inputDownStation, sectionRequest.getDistance());

        if(sections.size() == 0) {
            sections.add(newSection);
            return;
        }

        List<Station> sortedStations = getSortedStations();
        Station terminalStationByBaseSections = sortedStations.get(sortedStations.size() - 1);

        if(!terminalStationByBaseSections.equals(inputUpStation)) {
            throw new InvalidRequestException("새로운 구간의 상행역은 기존 구간의 하행종점역 이어야 합니다.");
        }

        if(sortedStations.contains(inputDownStation)) {
            throw new InvalidRequestException("새로운 구간의 하행역은 기존 구간에 포함된 역이 아니어야 합니다.");
        }

        sections.add(newSection);
    }

    public void deleteSection(Station inputStation) {
        if(sections.size() < 2) {
            throw new InvalidRequestException("구간이 2개 이상일때만 삭제할 수 있습니다.");
        }

        List<Station> sortedStations = getSortedStations();
        final Station downTerminalStation = sortedStations.get(sortedStations.size() - 1);

        if(!downTerminalStation.equals(inputStation)) {
            throw new InvalidRequestException("이 구간의 마지막 역만 삭제할 수 있습니다.");
        }

        Section targetSection = getSectionByDownStation(downTerminalStation);
        sections.remove(targetSection);
    }

    public List<Station> getSortedStations() {
        Station upTerminalStation = getUpTerminalStation();
        Section sectionByUpStation = getSectionByUpStation(upTerminalStation);

        List<Station> sortedStations = new ArrayList<>();
        while(sectionByUpStation != null) {
            if(sortedStations.isEmpty()) {
                sortedStations.add(sectionByUpStation.getUpStation());
            }

            Station downStation = sectionByUpStation.getDownStation();
            sortedStations.add(downStation);
            sectionByUpStation = getSectionByUpStation(downStation);
        }
        return sortedStations;
    }

    private Section getSectionByUpStation(Station upTerminalStation) {
        for(Section section : sections) {
            if(section.getUpStation().equals(upTerminalStation)) {
                return section;
            }
        }
        return null;
    }

    private Section getSectionByDownStation(Station downTerminalStation) {
        for(Section section : sections) {
            if(section.getDownStation().equals(downTerminalStation)) {
                return section;
            }
        }
        return null;
    }

    private Station getUpTerminalStation() {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();

        for(Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }

        upStations.removeAll(downStations);

        if(upStations.size() > 0) {
            return upStations.get(0);
        }
        return null;
    }
}
