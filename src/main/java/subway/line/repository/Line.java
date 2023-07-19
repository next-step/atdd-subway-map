package subway.line.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.repository.Section;
import subway.section.repository.Sections;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public
class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;

    @Embedded
    private Sections sections;
    @Column(name = "total_distance", nullable = false)
    private Long totalDistance;

    @Builder
    Line(String name, String color, Section initSection) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(List.of(initSection));
        this.totalDistance = initSection.getDistance();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
