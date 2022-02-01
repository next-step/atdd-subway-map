package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.exception.LogicError;
import nextstep.subway.exception.LogicException;

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

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public Section addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(this, upStation, downStation, distance);

        if (!sections.isEmpty()) {
            checkLastDownStation(section.getUpStation());
            checkExistStationInLine(section.getDownStation());
        }

        sections.add(section);
        return section;
    }

    private void checkExistStationInLine(Station downStation) {
        boolean exist = this.sections.stream()
                .anyMatch(section -> section.getId() == downStation.getId());

        if(exist){
            throw new LogicException(LogicError.EXIST_STATION_IN_LINE);
        }
    }

    private void checkLastDownStation(Station upStation) {
        if(upStation.getId() != this.getLastStation().getId()){
            throw new LogicException(LogicError.NOT_LAST_DOWN_STATION);
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStation() {
        List<Station> stations = new ArrayList<>();

        if (!sections.isEmpty()) {
            stations.add(sections.get(0).getUpStation());
            sections.forEach(section -> stations.add(section.getDownStation()));
        }

        return stations;
    }

    public Station getLastStation() {
        int size = sections.size();
        return sections.get(size - 1).getDownStation();
    }

    public void modify(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
    }
}
