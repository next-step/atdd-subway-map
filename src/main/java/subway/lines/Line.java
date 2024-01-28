package subway.lines;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import subway.section.Section;
import subway.station.Station;

@Entity
public class Line {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String color;

    private Long upStationId;
    private Long downStationId;
    private Long distance;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line() {

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

    public Long getDistance() {
        return distance;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void updatesSection(Long downStationId, Long distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Section> getSections() {
        return sections;
    }
}
