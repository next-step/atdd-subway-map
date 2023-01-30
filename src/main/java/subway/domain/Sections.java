package subway.domain;

import subway.common.exception.AlreadyExistException;
import subway.common.exception.NoDeleteOneSectionException;
import subway.common.exception.NoLastSectionException;
import subway.common.exception.NoRegisterStationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static subway.common.error.LineSectionError.*;

@Embeddable
public class Sections {

    private static final int ONE_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(final List<Section> sections) {
        return new Sections(sections);
    }

    public void addSection(final Line line, final Section section) {
        final Section findSection = findLastSectionByStation();
        validateAddStation(findSection, section);
        section.addLine(line);
        this.sections.add(section);
    }

    public void removeSection(final Station station) {
        validateOnlyOneSection();
        final Section lastSection = findLastSectionByStation();
        validateMatchLastStation(lastSection, station);
        this.sections.remove(lastSection);
    }

    public List<Section> getSections() {
        return sections;
    }

    private void validateAddStation(final Section findSection, final Section section) {
        validateMatchDownStation(findSection, section.getUpStation());
        validateNoneMatchStation(section.getDownStation());
    }

    private void validateOnlyOneSection() {

        if (this.sections.size() == ONE_SECTION) {
            throw new NoDeleteOneSectionException(NO_DELETE_ONE_SECTION.getMessage());
        }
    }

    private void validateMatchDownStation(final Section findSection, final Station upStation) {
        extracted(findSection, upStation, NO_REGISTER_UP_STATION.getMessage());
    }

    private void validateMatchLastStation(final Section findSection, final Station upStation) {
        extracted(findSection, upStation, NO_REGISTER_LAST_LINE_STATION.getMessage());
    }

    private void extracted(final Section findSection, final Station upStation, final String message) {
        if (canNotMatchDownStation(findSection, upStation)) {
            throw new NoRegisterStationException(message);
        }
    }

    private void validateNoneMatchStation(final Station downStation) {
        if (canMatchStation(downStation)) {
            throw new AlreadyExistException(NO_REGISTER_DOWN_STATION.getMessage());
        }
    }

    private boolean canMatchStation(final Station station) {
        return this.sections.stream()
                .anyMatch(section -> section.matchUpStation(station) || section.matchDownStation(station));
    }

    private boolean canNotMatchDownStation(final Section findSection, final Station station) {
        return !findSection.matchDownStation(station);
    }

    private Section findLastSectionByStation() {
        return this.sections.stream()
                .sorted(Comparator.comparing(Section::getId).reversed())
                .findFirst()
                .orElseThrow(() -> new NoLastSectionException(NO_LAST_SECTION.getMessage()));
    }
}
