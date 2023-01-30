package subway.domain;

import subway.common.exception.AlreadyExistException;
import subway.common.exception.NoDeleteOneSectionException;
import subway.common.exception.NoRegisterStationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        validateAddStation(section);
        section.addLine(line);
        this.sections.add(section);
    }

    public void removeSection(final Station station) {
        validateOnlyOneSection();
        final Section lastSection = findLastSectionByStation(station);
        this.sections.remove(lastSection);
    }

    public List<Section> getSections() {
        return sections;
    }

    private void validateAddStation(final Section section) {
        validateMatchDownStation(section.getUpStation());
        validateNoneMatchStation(section.getDownStation());
    }

    private void validateOnlyOneSection() {

        if (this.sections.size() == ONE_SECTION) {
            throw new NoDeleteOneSectionException("구간이 1개인 경우 삭제할 수 없습니다.");
        }
    }

    private void validateMatchDownStation(final Station upStation) {
        if (canMatchDownStation(upStation)) {
            throw new NoRegisterStationException("노선에 등록된 하행종점역이 없습니다.");
        }
    }

    private void validateNoneMatchStation(final Station downStation) {
        if (canMatchStation(downStation)) {
            throw new AlreadyExistException("노선에 등록된 역이 존재합니다.");
        }
    }

    private boolean canMatchStation(final Station station) {
        return this.sections.stream()
                .anyMatch(section -> section.matchUpStation(station) || section.matchDownStation(station));
    }

    private boolean canMatchDownStation(final Station station) {
        return this.sections.stream()
                .noneMatch(section -> section.matchDownStation(station));
    }

    private Section findLastSectionByStation(final Station station) {
        return this.sections.stream()
                .sorted(Comparator.comparing(Section::getId).reversed())
                .filter(section -> section.getDownStation().equals(station))
                .findFirst()
                .orElseThrow(() -> new NoRegisterStationException("해당 역으로 등록된 마지막 구간이 존재하지 않습니다."));
    }
}
