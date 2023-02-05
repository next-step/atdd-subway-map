package subway.line.domain;

import lombok.*;
import subway.line.dto.LineModifyRequest;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Embedded
    private Sections sections = new Sections();

    @Builder
    protected Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public void modify(LineModifyRequest request) {
        modifyForName(request.getName());
        modifyForColor(request.getColor());
    }

    private void modifyForName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    private void modifyForColor(String color) {
        if (color != null && !color.isBlank()) {
            this.color = color;
        }
    }

    public Section addSection(Station upStation, Station downStation, int distance) {
        return sections.addSection(
                Section.builder()
                        .distance(distance)
                        .line(this)
                        .upStation(upStation)
                        .downStation(downStation)
                        .build()
        );
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

}