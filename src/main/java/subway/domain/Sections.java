package subway.domain;

import org.springframework.util.CollectionUtils;
import subway.common.exception.AlreadyExistException;
import subway.common.exception.NoDeleteOneSectionException;
import subway.common.exception.NoLastSectionException;
import subway.common.exception.NoRegisterStationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static subway.common.error.LineSectionError.*;

@Embeddable
public class Sections {

    private static final int ONE_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void addSection(final Line line, final Station upStation, final Station downStation, final Integer distance) {
        final Section section = new Section(line, upStation, downStation, distance);
        final Section findSection = findLastSection();
        validateAddStation(findSection, section);
        section.addLine(line);
        this.sections.add(section);
    }

    public void removeSection(final Station station) {
        validateOnlyOneSection();
        final Section lastSection = findLastSection();
        validateMatchLastStation(lastSection, station);
        this.sections.remove(lastSection);
    }

    public void addLine(final Line line) {
        for (Section section : this.sections) {
            section.addLine(line);
        }
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
        validateMatchStation(findSection, upStation, NO_REGISTER_UP_STATION.getMessage());
    }

    private void validateMatchLastStation(final Section findSection, final Station upStation) {
        validateMatchStation(findSection, upStation, NO_REGISTER_LAST_LINE_STATION.getMessage());
    }

    private void validateMatchStation(final Section findSection, final Station upStation, final String message) {
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

    private Section findLastSection() {
        if (CollectionUtils.isEmpty(this.sections)) {
            throw new NoLastSectionException(NO_LAST_SECTION.getMessage());
        }
        return this.sections.get(this.sections.size() -1);
    }

    public List<Section> getSections() {
        return sections;
    }
}
