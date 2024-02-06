package subway.line.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import subway.section.domain.Section;
import subway.station.domain.Station;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE line SET deleted_at = CURRENT_TIMESTAMP where line_id = ?")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    @Column
    private Integer distance;

    @Column
    private Timestamp deleted_at;

    protected Line() {
    }

    private Line(String name, String color, Integer distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public static Line from(String name, String color, Integer distance) {
        return new Line(name, color, distance);
    }

    public void updateLine(String color, Integer distance) {
        this.color = color;
        this.distance = distance;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        return stations;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
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

    public Integer getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }

}
