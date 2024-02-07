package subway.lines;


import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import subway.section.Section;
import subway.section.Sections;

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

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;

        this.sections.addSection(new Section(this, upStationId, downStationId, distance));
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

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void deleteSection(Section section) {
        validateToDelete(section);

        downStationId = section.getUpStationId();
        distance -= section.getDistance();
        sections.deleteSection(section);
    }

    public void addSection(Long upStationId, Long downStationId, Long distance) {
        final Section sectionToAdd = new Section(this, upStationId, downStationId, distance);

        validateToAdd(sectionToAdd);
        sections.addSection(sectionToAdd);
        this.downStationId = downStationId;
        this.distance += distance;
    }

    private void validateToAdd(Section sectionToAdd) {
        final Set<Long> stationIdSet = new HashSet<>();
        sections.getSections().forEach(section -> {
            stationIdSet.add(section.getUpStationId());
            stationIdSet.add(section.getDownStationId());
        });

        if (stationIdSet.contains(sectionToAdd.getDownStationId())) {
            throw new IllegalArgumentException("duplicated station add request");
        }

        if (!Objects.equals(sectionToAdd.getUpStationId(), downStationId)) {
            throw new IllegalArgumentException("wrong station to add");
        }
    }

    private void validateToDelete(Section section) {
        if (sections.size() == 1) {
            throw new IllegalArgumentException("delete single section not allowed");
        }

        if(!Objects.equals(section.getDownStationId(), downStationId)) {
            throw new IllegalArgumentException("delete not a down station is not allowed");
        }
    }
}
