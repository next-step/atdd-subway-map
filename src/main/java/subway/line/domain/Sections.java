package subway.line.domain;

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
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다");
        }

        final Section lastSection = elements.get(elements.size() - 1);
        if (lastSection.canBeConnect(section)) {
            elements.add(section);
            return;
        }
        throw new IllegalArgumentException(
            String.format(
                "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다. lastSection: %s, newSection: %s",
                lastSection,
                section
            )
        );
    }

    public List<Section> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
