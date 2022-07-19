package nextstep.subway.domain;

import nextstep.subway.exception.NotFoundException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public int size() {
        return this.sections.size();
    }

    public Section findSectionByDownStationId(Long downStationId) {
        return this.sections.stream()
                .filter(s -> s.getDownStationId().equals(downStationId))
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("%d번 역을 찾지 못했습니다.", downStationId)));
    }

    public List<Long> getAllStationIds() {
        return this.sections.stream()
                .map(section -> List.of(section.getUpStationId(), section.getDownStationId()))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Long getFinalDownStationId(Long downStationId) {
        List<Long> upStationIds = getUpStationIds();
        List<Long> downStationIds = getDownStationIds();

        return downStationIds.stream()
                .filter(down -> !upStationIds.contains(down))
                .findAny()
                .orElse(downStationId);
    }

    private List<Long> getUpStationIds() {
        return this.sections.stream().map(Section::getUpStationId).collect(Collectors.toList());
    }

    private List<Long> getDownStationIds() {
        return this.sections.stream().map(Section::getDownStationId).collect(Collectors.toList());
    }
}
