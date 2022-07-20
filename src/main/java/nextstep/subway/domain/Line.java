package nextstep.subway.domain;


import lombok.Getter;
import nextstep.subway.applicaion.exceptions.InvalidStationParameterException;
import nextstep.subway.enums.exception.ErrorCode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections;


    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Integer distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(this, upStation, downStation, distance);
    }


    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Line line, Station upStation, Station downStation, Integer distance) {
        sections.addSection(new Section(line, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void validSameAlreadyExistDownStationAndReqUpStation(Station upStation) {
        if (!Objects.equals(sections.getLastStation().getName(), upStation.getName())) {
            throw new InvalidStationParameterException(ErrorCode.SAME_STATION);
        }
    }
}
