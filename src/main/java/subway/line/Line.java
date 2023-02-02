package subway.line;

import subway.section.Section;
import subway.section.SectionException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    private int distance;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        validateAddable(section);
        this.sections.add(section);
        this.downStationId = section.getDownStationId();
    }

    public void deleteSection(Long stationId) {
        validateDeletable(stationId);
        sections.stream()
                .filter(section -> section.getDownStationId().equals(stationId))
                .findAny()
                .ifPresent(section -> sections.remove(section));
    }

    private void validateDeletable(Long stationId) {
        validateEndStationOfLine(stationId);
        validateMinimumSection();
        validateContainsSection(stationId);
    }

    private void validateMinimumSection() {
        if (sections.size() == 1) {
            throw new SectionException("노선에 구간이 한개만 존재하여 구간을 삭제할 수 없습니다.");
        }
    }

    private void validateContainsSection(Long stationId) {
        if (!isContainsSection(stationId)) {
            throw new SectionException("노선에 포함되지 않은 구간을 삭제할 수 없습니다.");
        }
    }

    private void validateAddable(Section section) {
        if (this.sections.size() > 0) {
            validateEndStationOfLine(section.getUpStationId());
            validateDuplicateSection(section.getDownStationId());
        }
    }

    private void validateEndStationOfLine(Long stationId) {
        if (!downStationId.equals(stationId)) {
            throw new SectionException("노선의 하행 종점역 이외의 구간은 추가 및 삭제할 수 없습니다.");
        }
    }

    private void validateDuplicateSection(Long stationsId) {
        if (isContainsSection(stationsId)) {
            throw new SectionException("노선에 중복된 구간은 추가할 수 없습니다.");
        }
    }

    private boolean isContainsSection(Long stationId) {
        return this.sections.stream()
                .anyMatch(section -> section.getUpStationId().equals(stationId)
                        || section.getDownStationId().equals(stationId));
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

    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }
}
