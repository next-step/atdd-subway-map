package nextstep.subway.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.entity.collections.Sections;

import javax.persistence.*;
import java.util.List;

import static java.util.Objects.requireNonNull;

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
    @Column(nullable = false)
    private Integer distance;

    @Embedded
    private Sections sections = new Sections();


    @Builder(toBuilder = true)
    protected Line(Long id, String name, String color, Integer distance) {
        requireNonNull(name, "name is required");
        requireNonNull(color, "color is required");
        requireNonNull(distance, "distance is required");

        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void updateNameAndColor(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(
                section.toBuilder()
                        .line(this)
                        .build());
    }

    public void deleteStation(Station station) {
        sections.deleteStation(station);
    }
}
