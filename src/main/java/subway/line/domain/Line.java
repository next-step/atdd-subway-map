package subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    @Builder
    private Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static Line createLine(final String name, final String color,
                                  final Long distance, Station upStation, Station downStation) {
        Line line = Line.builder()
                .name(name)
                .color(color)
                .build();

        createSections(distance, line, upStation, downStation);

        return line;
    }

    private static void createSections(final Long distance, final Line line,
                                       final Station upStation, final Station downStation) {
        Section section = Section.createSection(line, upStation, downStation, distance);
        line.getSections().createFirstSection(section);
    }


    /**
     * 지하철 노선 정보를 수정합니다.
     *
     * @param line 수정할 지하철 노선 정보
     */
    public void updateLine(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
