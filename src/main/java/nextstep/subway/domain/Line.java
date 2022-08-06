package nextstep.subway.domain;

import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateRequest;
import nextstep.subway.applicaion.dto.LineUpdateRequest;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    private Long distance;

    public Line(LineCreateRequest lineCreateRequest, Section section) {
        this.name = lineCreateRequest.getName();
        this.color = lineCreateRequest.getColor();
        section.setLine(this);
        this.sections.add(section);
        this.distance = lineCreateRequest.getDistance();
    }

    public Line(Long id, String name, String color, Sections sections, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;
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

    public Long getDistance() {
        return this.distance;
    }

    public void update(Line updateLine, Station upStation, Station downStation) {
        if (updateLine.getName() != null) {
            this.name = updateLine.getName();
        }

        if (updateLine.getColor() != null) {
            this.color = updateLine.getColor();
        }

        if (updateLine.getDistance() != null) {
            this.distance = updateLine.getDistance();
        }

        this.sections.get(0).update(upStation, downStation, updateLine.getDistance());
    }

    public void addSection(Section section) {
        this.sections.checkSectionCreateRulesOrThrow(section.getUpStationId(), section.getDownStationId());

        this.sections.add(section);
        section.setLine(this);
        this.distance = getDistance();
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public void removeSection(Long stationId) {
        this.sections.removeSection(stationId);
    }
}
