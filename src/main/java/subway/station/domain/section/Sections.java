package subway.station.domain.section;

import lombok.Getter;
import subway.station.domain.station.Station;
import subway.station.global.error.exception.ErrorCode;
import subway.station.global.error.exception.InvalidValueException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        addValidationCheck(section);
        sections.add(section);
    }

    public void deleteSection(Station station) {
        Section section = findSection(station);
        deleteValidationCheck(section);
        sections.remove(section);
    }

    private void addValidationCheck(Section section) {
        if (this.getSections().size() == 0) {
            return;
        }
        if (!this.getSections().get(this.getSections().size() - 1).getDownStation().equals(section.getUpStation())) {
            throw new InvalidValueException(ErrorCode.MISMATCHED_BETWEEN_UP_STATION_OF_NEW_SECTION_AND_DOWN_STATION_OF_LINE);
        }
        for (Section s : this.getSections()) {
            if (s.getUpStation().equals(section.getDownStation()) || s.getDownStation().equals(section.getDownStation())) {
                throw new InvalidValueException(ErrorCode.ALREADY_REGISTERED_IN_LINE);
            }
        }
    }

    private void deleteValidationCheck(Section section) {
        if (this.getSections().size() == 1) {
            throw new InvalidValueException(ErrorCode.LINE_HAS_ONLY_ONE_SECTION);
        }
        if (!this.getSections().get(this.getSections().size() - 1).equals(section)) {
            throw new InvalidValueException(ErrorCode.SECTION_IS_NOT_END_OF_LINE);
        }
    }

    private Section findSection(Station station) {
        return this.getSections().stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException(ErrorCode.STATION_NOT_EXISTS_SECTION));
    }
}
