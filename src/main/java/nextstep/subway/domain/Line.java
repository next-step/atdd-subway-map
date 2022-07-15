package nextstep.subway.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineUpdateRequest;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    @Builder
    public Line(String name, String color, Station upStation, Station downStation, long distance ) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(LineUpdateRequest request) {
        this.name = request.getName();
        this.color = request.getColor();
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void deleteSection(Station station) {
        this.sections.delete(station);
    }

    public List<Station> getAllStations() {
        return sections.getAllStations();
    }
}
