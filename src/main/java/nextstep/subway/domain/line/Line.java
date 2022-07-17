package nextstep.subway.domain.line;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import nextstep.subway.domain.section.Sections;
import nextstep.subway.domain.station.Station;

@Getter
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @Embedded
    private Sections sections;

    public Line() {}

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections = Sections.of(this, upStation, downStation, distance);
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, Long distance) {
        this.sections.add(this, upStation, downStation, distance);
    }

    public void deleteSection(Long stationId) {
        this.sections.delete(stationId);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
