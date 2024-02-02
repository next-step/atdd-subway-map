package subway.lines;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.section.Section;
import subway.section.SectionAddRequest;
import subway.section.SectionDeleteRequest;

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

    @OneToMany(mappedBy = "line", cascade={CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
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

    public void deleteSection(Section section) {
        downStationId = section.getUpStationId();
        distance -= section.getDistance();
        sections.remove(section);
    }

    public void addSection(Section section) {
        sections.add(section);
        downStationId = section.getDownStationId();
        distance += section.getDistance();
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Section> getSections() {
        return sections;
    }
}
