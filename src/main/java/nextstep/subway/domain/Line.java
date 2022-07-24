package nextstep.subway.domain;


import lombok.Getter;
import nextstep.subway.applicaion.exceptions.InvalidStationParameterException;
import nextstep.subway.applicaion.exceptions.SectionArrayOutOfBoundException;
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
        sections.validAlreadyExistStation(upStation);
        sections.validContainAlreadyReqDownStation(downStation);
        sections.addSection(new Section(line, upStation, downStation, distance));
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(Long downStationId) {
        if (sections.getStations().size() < 2)
            throw new SectionArrayOutOfBoundException(ErrorCode.NOT_ENOUGH_SECTION);
        sections.deleteSection(downStationId);
    }
}
