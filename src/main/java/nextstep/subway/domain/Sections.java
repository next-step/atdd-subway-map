package nextstep.subway.domain;

import nextstep.subway.exception.DuplicateDownStationException;
import nextstep.subway.exception.NonMatchLastStationException;
import nextstep.subway.exception.SectionNotFoundException;
import nextstep.subway.exception.UnableRemoveSectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    private static final String UP_STATION_ID = "upStationId";
    private static final String DOWN_STATION_ID = "downStationId";

    @OneToMany(mappedBy = "subwayLine", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        validNonMatchLastStation(newSection.getUpStationId(), UP_STATION_ID);
        validDuplicatedDownStation(newSection.getDownStationId());

        this.sections.add(newSection);
    }

    public Section remove(Long downStationId) {
        validNonMatchLastStation(downStationId, DOWN_STATION_ID);
        validRemoveSection();
        Section section = getSectionById(downStationId);
        this.sections.remove(section);
        return section;
    }

    public Set<Long> getStationIds() {
        if (this.sections.isEmpty()) {
            return Collections.emptySet();
        }
        return Stream.of(sections.stream().map(Section::getUpStationId),
                        sections.stream().map(Section::getDownStationId))
                .flatMap(stream -> stream)
                .collect(Collectors.toSet());
    }

    private Section getSectionById(Long stationId) {
        return this.sections.stream()
                .filter(item -> item.isDownStationIdFor(stationId))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException(stationId));
    }

    private void validRemoveSection() {
        if (this.sections.size() <= 1) {
            throw new UnableRemoveSectionException();
        }
    }

    private void validNonMatchLastStation(Long downStationId, String message) {
        if (isNonMatchLastStation(downStationId)) {
            throw new NonMatchLastStationException(message);
        }
    }

    private boolean isNonMatchLastStation(Long stationId) {
        return !sections.isEmpty()
                && !stationId.equals(getLastDownStationId());
    }

    private Long getLastDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }

    private void validDuplicatedDownStation(Long downStationId) {
        if (isDuplicatedDownStation(downStationId)) {
            throw new DuplicateDownStationException();
        }
    }

    private boolean isDuplicatedDownStation(Long downStationId) {
        return getStationIds().contains(downStationId);
    }

}
