package nextstep.subway.domain.Line;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.section.Sections;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long lineId;

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections.createSection(this, upStation, downStation, distance);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
    public Long distance() {
        return this.sections.distance();
    }

    public Station upStation() {
        return this.sections.upStation();
    }

    public Station downStation() {
        return this.sections.downStation();
    }

    public void validDeleteDownStation(Station downStation) {
        sections.validDelete(downStation);
    }

    public void addSection(Station upStation, Station downStation, Long distance) {
        this.sections.addSection(this, upStation, downStation, distance);
    }

}
