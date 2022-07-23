package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Embeddable
public class Sections {

    @OneToMany(
            mappedBy = "line",
            orphanRemoval = true,
            cascade = { CascadeType.PERSIST, CascadeType.REMOVE }
    )

    private List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        if (isSectionsEmpty()) {
            this.sections.add(newSection);
            return;
        }
        Section latestSection = sections.get(sections.size() - 1);
        if (!latestSection.getDownStationId().equals(newSection.getUpStationId())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }
        if (getLineStationIds().contains(newSection.getDownStationId())) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");
        }
        this.sections.add(newSection);
    }

    private boolean isSectionsEmpty() {
        return sections.size() == 0;
    }

    public Set<Long> getLineStationIds() {
        Set<Long> stationIds = new HashSet<>();
        sections.forEach((section) -> {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        });
        return stationIds;
    }


}
