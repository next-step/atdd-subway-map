package nextstep.subway.domain;

import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.exceptions.NotMatchedSectionRuleException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void checkSectionRuleOrThrow(Long upStationId, Long downStationId) {
        if (this.sections.equalCurrentDownStationIdWithNewUpStationId(upStationId)) {
            throw new NotMatchedSectionRuleException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역과 같아야 합니다.");
        }
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }
}
