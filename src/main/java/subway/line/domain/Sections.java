package subway.line.domain;

import subway.line.domain.exception.SectionAddFailException;
import subway.line.domain.exception.SectionRemoveFailException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(
        mappedBy = "line",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Section> elements = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> elements) {
        if (elements.size() == 0) {
            throw new IllegalArgumentException();
        }
        this.elements = elements;
    }

    public Sections(Section... elements) {
        this(
            Arrays.stream(elements)
                .collect(Collectors.toList())
        );
    }

    public void add(Section section) {
        if (elements.size() == 0) {
            elements.add(section);
            return;
        }

        if (elements.stream().anyMatch(it -> it.containsLastStation(section))) {
            throw new SectionAddFailException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다");
        }

        final Section lastSection = findLastSection();
        if (lastSection.canBeConnect(section)) {
            elements.add(section);
            return;
        }
        throw new SectionAddFailException(
            String.format(
                "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다. lastSection: %s, newSection: %s",
                lastSection,
                section
            )
        );
    }

    public void removeSection(Long stationId) {
        if (elements.size() == 1) {
            throw new SectionRemoveFailException("구간이 1개인 경우 삭제할 수 없습니다.");
        }

        final Section lastSection = findLastSection();
        if (lastSection.isSameDownStation(stationId)) {
            elements.remove(lastSection);
            return;
        }
        throw new SectionRemoveFailException("마지막 구간만 삭제 가능합니다. stationId: " + stationId);
    }

    private Section findLastSection() {
        return elements.stream()
            .filter(it -> isLastSection(it))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException());
    }

    private boolean isLastSection(Section section) {
        return elements.stream()
            .noneMatch(it -> section.canBeConnect(it));
    }

    public List<Section> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
