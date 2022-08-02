package nextstep.subway.domain;

import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;

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

    public Line(LineRequest lineRequest, Section section) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.sections.add(section);
        section.setLine(this);
        this.distance = lineRequest.getDistance();
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

    public void update(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName() != null ? lineRequest.getName() : this.name;
        this.color = lineRequest.getColor() != null ? lineRequest.getColor() : this.color;
        this.sections.get(0).update(upStation, downStation, lineRequest.getDistance());
        this.distance = lineRequest.getDistance() != null ? lineRequest.getDistance() : this.distance;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
        this.distance = getDistance();
    }

    public void checkSectionRulesOrThrow(Long upStationId, Long downStationId) {
        this.sections.checkSectionRulesOrThrow(upStationId, downStationId);
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public Section getLastSection(Long stationId) {
        return this.sections.getLastSection(stationId);
    }

    public void removeSection() {
        this.sections.removeSection();
    }
}
