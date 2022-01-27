package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    private Line(final String name, final String color, final Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(final String name, final String color, final Long upStationId, final Long downStationId, int distance) {
        this(name, color, new Sections(new Section(upStationId, downStationId, distance)));
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Long upStationId, Long downStationId, int distance) {
        sections.addSection(upStationId, downStationId, distance);
    }

    public boolean equalsName(String name) {
        return Objects.equals(this.name, name);
    }

    public List<Long> getAllStations() {
        return sections.getAllStations();
    }

    public void deleteSection(final Long stationId) {
        sections.deleteSection(stationId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
