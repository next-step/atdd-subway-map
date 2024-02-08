package subway.line.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import subway.section.entity.Section;
import subway.station.entity.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Column(nullable = false)
    private Long distance;

    @Builder
    public Line(String name, String color, List<Section> sections, Long distance) {
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, Long distance) {
        Section section = new Section();
        section.updateSection(upStation, downStation, distance);
        this.getSections().add(section);
    }
}
