package subway.line.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.station.entity.Station;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Sections sections;

    @Builder
    public Line(
            String name,
            String color,
            Station upStation,
            Station downStation,
            Integer distance
    ) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(
                new LinkedList<>(Collections.singletonList(
                        Section.builder()
                                .upStation(upStation)
                                .downStation(downStation)
                                .distance(distance)
                                .build()
                        )
                )
        );
    }

    public void updateLine(Line updateLine) {
        this.name = updateLine.name;
        this.color = updateLine.color;
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }
}
