package subway.lines;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public void updateSectionInfo(Long downStationId, Long distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void validateSectionToAdd(SectionAddRequest sectionAddRequest) {
        final Set<Long> stationIdSet = new HashSet<>();
        sections.forEach(section -> {
            stationIdSet.add(section.getUpStationId());
            stationIdSet.add(section.getDownStationId());
        });

        if (stationIdSet.contains(sectionAddRequest.getDownStationId())) {
            throw new IllegalArgumentException();
        }

        if (!Objects.equals(downStationId, sectionAddRequest.getUpStationId())) {
            throw new IllegalArgumentException();
        }
    }

    public void validateSectionCanBeDeleted(SectionDeleteRequest sectionDeleteRequest) {
        if (sections.size() == 1) {
            throw new IllegalArgumentException();
        }

        if(!Objects.equals(downStationId, sectionDeleteRequest.getStationId())) {
            throw new IllegalArgumentException();
        }
    }
}
