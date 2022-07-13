package nextstep.subway.domain;

import nextstep.subway.exception.DuplicateDownStationException;
import nextstep.subway.exception.NonMatchLastStationException;
import nextstep.subway.exception.NonMatchLastStationException.Message;
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

import static nextstep.subway.exception.NonMatchLastStationException.Message.DOWN_STATION_ID;
import static nextstep.subway.exception.NonMatchLastStationException.Message.UP_STATION_ID;

@Embeddable
public class Sections {

    @OneToMany(
            mappedBy = "subwayLine",
            orphanRemoval = true,
            cascade = { CascadeType.PERSIST, CascadeType.REMOVE }
    )
    private List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        validateNonMatchLastStation(newSection.getUpStationId(), UP_STATION_ID);
        validDuplicatedDownStation(newSection.getDownStationId());

        this.sections.add(newSection);
    }

    public void remove(Long downStationId) {
        validateNonMatchLastStation(downStationId, DOWN_STATION_ID);
        validateRemoveSection();

        Section section = getSectionById(downStationId);
        this.sections.remove(section);
        section.clearSubwayLine();
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

    private void validateRemoveSection() {
        if (this.sections.size() <= 1) {
            throw new UnableRemoveSectionException();
        }
    }

    private void validateNonMatchLastStation(Long stationId, Message message) {
        if (this.sections.isEmpty()) {
            return;
        }
        if (!isLastStation(stationId)) {
            throw new NonMatchLastStationException(message);
        }
    }

    private boolean isLastStation(Long stationId) {
        return stationId.equals(getLastDownStationId());
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
