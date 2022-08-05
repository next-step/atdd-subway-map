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

    public void update(LineUpdateRequest lineUpdateRequest, Station upStation, Station downStation) {
        if (lineUpdateRequest.getName() != null) {
            this.name = lineUpdateRequest.getName();
        }

        if (lineUpdateRequest.getColor() != null) {
            this.color = lineUpdateRequest.getColor();
        }

        if (lineUpdateRequest.getDistance() != null) {
            this.distance = lineUpdateRequest.getDistance();
        }

        this.sections.get(0).update(upStation, downStation, lineUpdateRequest.getDistance());
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
