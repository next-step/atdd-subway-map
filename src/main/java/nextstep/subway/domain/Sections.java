package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.SectionRequest;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(
            mappedBy = "line",
            orphanRemoval = true,
            cascade = { CascadeType.PERSIST, CascadeType.REMOVE }
    )

    private List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            this.sections.add(newSection);
            return;
        }

        newSectionUpStationValidation(newSection.getUpStationId());
        newSectionDownStationValidation(newSection.getDownStationId());

        this.sections.add(newSection);
    }


    private void newSectionUpStationValidation(Long upStationId) {
        Section latestSection = sections.get(sections.size() - 1);

        if (!latestSection.getDownStationId().equals(upStationId)) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }
    }

    private void newSectionDownStationValidation(Long downStationId) {
        if (getLineStationIds().contains(downStationId)) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");
        }
    }


    public void remove(Long upStationId, Long downStationId) {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("구간을 더이상 삭제할 수 없습니다.");
        }

        Section sectionToRemove = sections.stream().filter((section) -> section.getDownStationId().equals(downStationId)
                && section.getUpStationId().equals(upStationId)).findFirst().orElseThrow(() -> new IllegalArgumentException("유효하지 않은 구간입니다."));

        if (isLastSection(sectionToRemove)) {
            sections.remove(sectionToRemove);
            return;
        }

        throw new IllegalArgumentException("마지막 구간이 아닙니다.");
    }

    public Set<Long> getLineStationIds() {
        Set<Long> stationIds = new HashSet<>();

        sections.forEach((section) -> {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        });

        return stationIds;
    }

    private boolean isLastSection(Section sectionToRemove) {
        return sections.indexOf(sectionToRemove) == this.sections.size() - 1;
    }
}
