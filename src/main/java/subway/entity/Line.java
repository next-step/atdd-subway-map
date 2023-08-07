package subway.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.enums.SubwayMessage;
import subway.exception.SubwayException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }
    public List<Station> getStations() {
        List<Station> list = sections.stream().map(Section::getDownStation).collect(Collectors.toList());
        list.add(sections.get(0).getUpStation());
        return list;
    }

    public void modifyLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        if(!sections.get(sections.size()-1).getDownStation().equals(upStation)){
            throw new SubwayException(SubwayMessage.NEW_SECTION_UP_STATION_ERROR);
        }

        if(getStations().stream().anyMatch(station -> station.equals(downStation))){
            throw new SubwayException(SubwayMessage.NEW_SECTION_NEW_STATION_ERROR);
        }

        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void deleteSection(Station station) {
        if(sections.size() == 1) {
            throw new SubwayException(SubwayMessage.SECTION_MORE_THAN_TWO);
        }
        if(!sections.get(sections.size()-1).getDownStation().equals(station)){
            throw new SubwayException(SubwayMessage.DELETE_STATION_MUST_END);
        }

        sections.remove(sections.size()-1);
    }
}
