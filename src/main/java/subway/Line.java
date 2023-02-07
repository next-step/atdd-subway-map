package subway;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "upStationId")
    private Long upStationId;

    @Column(name = "downStationId")
    private Long downStationId;

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {

    }

    public Line(String name, Long upStationId, Long downStationId) {
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void patch(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.upStationId = lineRequest.getUpStationId();
        this.downStationId = lineRequest.getDownStationId();
    }

    public void addSection(Section section) {
        if (!CollectionUtils.isEmpty(this.sections)) {
            validateAddSection(section);
        }

        this.sections.add(section);
    }

    private void validateAddSection(Section section) {
        if (hasContainSection(section)) {
            throw new IllegalStateException("하행역이 기존 구간에 등록되있는 역입니다.");
        }

        if (!equalsLastSectionDownStationId(section.getUpStationId())) {
            throw new IllegalStateException("새로운 구간의 상행선은 해당 노선에 등록되어 있는 하행 종점역이여야 합니다.");
        }
    }

    private boolean hasContainSection(Section section) {
        return this.sections.stream().anyMatch(x -> x.getDownStationId().equals(section.getDownStationId())
                || x.getUpStationId().equals(section.getDownStationId()));
    }

    private boolean equalsLastSectionDownStationId(long stationId) {
        int lastSectionIndex = getLastSectionIndex();

        if (lastSectionIndex <= 0) {
            return true;
        }

        Section lastSection = this.sections.get(lastSectionIndex);
        if (lastSection.getDownStationId() == stationId) {
            return true;
        }

        return false;
    }

    public void deleteSection(Long stationId) {
        validateDeleteSection(stationId);
        this.sections.remove(getLastSectionIndex());
    }

    private void validateDeleteSection(Long stationId) {
        if (CollectionUtils.isEmpty(this.sections)) {
            throw new IllegalStateException("구간이 없는 상태에서는 삭제할 수 없습니다.");
        }

        if (this.sections.size() <= 1) {
            throw new IllegalStateException("구간이 하나일 경우 삭제할 수 없습니다.");
        }

        Section lastSection = getLastSection();
        if (!lastSection.getDownStationId().equals(stationId)) {
            throw new IllegalStateException("마지막 구간만 삭제할 수 있습니다.");
        }
    }

    private int getLastSectionIndex() {
        return this.sections.size() - 1;
    }

    private Section getLastSection() {
        return this.sections.get(getLastSectionIndex());
    }

    public Optional<Section> findSection(Long sectionId) {
        return this.sections.stream().filter(x -> x.getId() == sectionId).findFirst();
    }
}
