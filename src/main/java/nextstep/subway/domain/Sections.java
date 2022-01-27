package nextstep.subway.domain;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MINIMUM_SECTION_COUNT = 1;

    @Convert(converter = SectionConverter.class)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public Sections(final Section section) {
        this(Collections.singletonList(section));
    }

    public void addSection(final Long upStationId, final Long downStationId, final int distance) {
        if (upStationId.equals(downStationId) || !lastSection().hasDownStationId(upStationId)
                || upStationIds().contains(downStationId)
        ) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }

        this.sections.add(new Section(upStationId, downStationId, distance));
    }

    public List<Long> getAllStations() {
        List<Long> results = new ArrayList<>();

        List<Long> upStationIds = sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());

        results.addAll(upStationIds);
        results.add(lastSection().getDownStationId());

        return results;
    }

    public void deleteSection(final Long stationId) {
        if (this.sections.size() == MINIMUM_SECTION_COUNT || !lastSection().hasDownStationId(stationId)) {
            throw new IllegalArgumentException();
        }

        this.sections.remove(lastSection());
    }

    private Section lastSection() {
        return this.sections.get(sectionCount() - 1);
    }

    private int sectionCount() {
        return sections.size();
    }

    private List<Long> upStationIds() {
        return this.sections.stream().map(Section::getUpStationId).collect(Collectors.toList());
    }
}
