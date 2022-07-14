package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    static Sections of(List<Section> sections) {
        return new Sections(sections);
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Long getUpStationId() {
        return sections.get(0).getUpStationId();
    }

    public Long getDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }

    public Long getTotalDistance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
    }

    public void add(Section section) {
        sections.add(section);
    }

    boolean containsStationId(Long stationId) {
        return sections.stream()
                .anyMatch(section -> section.containsStationId(stationId));
    }

    public void deleteSection(Long stationId) {
        validateToDelete(stationId);
        sections.remove(sections.size() - 1);
    }

    private void validateToDelete(Long stationId) {
        if (sections.size() == 1) {
            throw new IllegalArgumentException("지하철 노선에 구간이 1개인 경우 역을 삭제할 수 없다.");
        }
        if (!Objects.equals(getDownStationId(), stationId)) {
            throw new IllegalArgumentException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.");
        }
    }
}
