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
        final Long upStationId = section.getUpStationId();
        final Long downStationId = section.getDownStationId();
        validateAddSection(upStationId, downStationId);
        sections.add(section);
    }

    private void validateAddSection(Long upStationId, Long downStationId) {
        if (!Objects.equals(upStationId, getDownStationId())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다.");
        }
        if (containsStationId(downStationId)) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
        }
    }

    private boolean containsStationId(Long stationId) {
        return sections.stream()
                .anyMatch(section -> section.containsStationId(stationId));
    }
}
