package subway.domain;

import subway.exception.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section newSection) {
        validate(newSection);
        sections.add(newSection);
    }

    private void validate(Section newSection) {
        if (sections.isEmpty()) {
            return;
        }

        if (!newSection.addable(lastSection())) {
            throw new SectionException("새로운 구간의 상행역은 해당 노선의 하행 종점역이어야 합니다.");
        }

        if (!newSection.addable(stations())) {
            throw new SectionException("새로운 구간의 하행역이 해당 노선에 등록되어있습니다.");
        }
    }

    public List<Station> stations() {
        return sections.stream()
                .map(section -> List.of(section.getUpStation(), section.getDownStation()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public void delete(long stationId) {
        if (sections.size() == 1) {
            throw new SectionException("구간이 1개인 경우 삭제할 수 없습니다.");
        }

        Section lastSection = lastSection();
        if (!lastSection.isDownStationId(stationId)) {
            throw new SectionException("마지막 구간만 제거할 수 있습니다.");
        }

        sections.remove(lastSection);
    }

    private Section lastSection() {
        return sections.get(sections.size() - 1);
    }

    public int distance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }
}
