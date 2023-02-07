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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
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

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private final Sections sections = new Sections();

    @Builder
    private Line(final Long id, final String name, final String color,
                 final Station upStation, final Station downStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    // == 생성 메서드 ==
    public static Line createLine(final String name, final String color,
                                  final Long distance, final Station upStation, final Station downStation) {
        Line line = Line.builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .build();

        createSections(distance, line, upStation, downStation);

        return line;
    }

    // == 연관관계 편의 메서드 ==
    private static void createSections(final Long distance, final Line line,
                                       final Station upStation, final Station downStation) {
        Section section = Section.createSection(line, upStation, downStation, distance);
        line.getSections().createSection(section);
    }


    // == 비즈니스 로직 ==
    public void updateLine(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void changeDownStation(final Station station) {
        if (this.downStation.equals(station)) {
            return;
        }

        this.downStation = station;
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
