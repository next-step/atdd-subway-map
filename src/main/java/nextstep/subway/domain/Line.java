package nextstep.subway.domain;

import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    private Long distance;

    public Line(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.sections.add(new Section(upStation, downStation, lineRequest.getDistance()));

        for (Section section : this.sections) {
            section.setLine(this);
        }
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

    public List<Section> getSections() {
        return sections;
    }

    public Long getDistance() {
        Long distanceSum = 0L;
        for (Section section : this.sections) {
            distanceSum += section.getDistance();
        }
        return distanceSum;
    }

    public Long getUpStationId() { return this.sections.get(0).getUpStationId(); }

    public Long getDownStationId() { return this.sections.get(0).getDownStationId(); }

    public void update(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName() != null ? lineRequest.getName() : this.name;
        this.color = lineRequest.getColor() != null ? lineRequest.getColor() : this.color;
        this.sections.get(0).update(upStation, downStation, lineRequest.getDistance());
        this.distance = lineRequest.getDistance() != null ? lineRequest.getDistance() : this.distance;
    }
}
