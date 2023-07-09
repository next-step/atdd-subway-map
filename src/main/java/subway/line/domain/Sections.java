package subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.util.CollectionUtils;

import subway.section.domain.Section;
import subway.section.exception.InvalidSectionCreateException;
import subway.section.exception.SectionNotFoundException;
import subway.support.ErrorCode;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Sections {

    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public boolean isLastDownStation(Long stationId) {
        if (CollectionUtils.isEmpty(this.sections)) {
            return false;
        }

        return Objects.equals(sections.get(sections.size()-1).getDownStation().getId(), stationId);
    }

    public boolean isLastSection() {
        if (CollectionUtils.isEmpty(this.sections)) {
            return false;
        }

        return this.sections.size() == 1;
    }

    public boolean isLastStation(Long stationId) {
        return Objects.equals(this.sections.get(0).getUpStation().getId(), stationId) ||
                Objects.equals(this.sections.get(0).getDownStation().getId(), stationId);
    }

    public void appendSection(Section section) {
        this.sections.add(section);
    }

    public Section get(Long downStationId, Long upStationId) {
        return sections.stream()
                       .filter(section -> section.isMe(upStationId, downStationId))
                       .findFirst()
                       .orElseThrow(SectionNotFoundException::new);
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean possibleToAddSection(Section section) {
        if (isEmpty()) {
            return true;
        }

        Section lastSection = getLastSection();

        if (!section.isUpstation(lastSection.getDownStation().getId())) {
            throw new InvalidSectionCreateException(ErrorCode.SECTION_CREATE_FAIL_BY_UPSTATION);
        }

        if (alreadyRegistered(section.getDownStation().getId())) {
            throw new InvalidSectionCreateException(ErrorCode.SECTION_CREATE_FAIL_BY_DOWNSTATION);
        }

        return true;
    }

    private boolean alreadyRegistered(Long stationId) {
        return sections.stream()
                       .anyMatch(section -> section.containStation(stationId));
    }

    private Section getLastSection() {
        return sections.get(sections.size()-1);
    }

}
