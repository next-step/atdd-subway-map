package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Embedded
    private Sections sections = new Sections();


    @Builder(toBuilder = true)
    protected Line(Long id, String name, String color) {
        requireNonNull(name, "name is required");
        requireNonNull(color, "color is required");

        this.id = id;
        this.name = name;
        this.color = color;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void updateNameAndColor(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, Integer distance) {
        this.sections.add(
                Section.builder()
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(distance)
                        .line(this)
                        .build());
    }

    public void deleteStation(Station station) {
        sections.delete(station);
    }
}
