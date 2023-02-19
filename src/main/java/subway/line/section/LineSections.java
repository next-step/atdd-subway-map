package subway.line.section;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.exception.CannotRemoveLineSectionException;
import subway.exception.NotFoundStationException;
import subway.station.Station;

@Embeddable
public class LineSections {
    @OneToMany(
        mappedBy = "line",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true
    )
    private List<LineSection> lineSections = new ArrayList<>();

    public Station getUpStation() {
        return getFirstLineSection().map(LineSection::getUpStation)
            .orElseThrow(NotFoundStationException::new);
    }

    public Station getDownStation() {
        return getLastLineSection().map(LineSection::getDownStation)
            .orElseThrow(NotFoundStationException::new);
    }

    public Optional<LineSection> getFirstLineSection() {
        return lineSections.stream()
            .min(Comparator.comparing(LineSection::getNumber));
    }

    public Optional<LineSection> getLastLineSection() {
        return lineSections.stream()
            .max(Comparator.comparing(LineSection::getNumber));
    }

    public void addLineSection(LineSection lineSection) {
        if (isAbleToRegister(lineSection)) {
            lineSection.setNumber(nextSectionNumber());
//            lineSection.setLine(this);
            lineSections.add(lineSection);
        }
    }

    public void removeLineSection(LineSection lineSection) {
        if (isNotAbleToRemove(lineSection)) {
            throw new CannotRemoveLineSectionException();
        }

        lineSections.remove(lineSection);
    }

    private int nextSectionNumber() {
        return lineSections.stream()
            .mapToInt(LineSection::getNumber)
            .max().orElse(0) + 1;
    }

    private boolean isAbleToRegister(LineSection lineSection) {
        LineSection lastLineSection = getLastLineSection()
            .orElse(null);

        return Objects.isNull(lastLineSection) || lastLineSection.isConnected(lineSection);
    }

    private boolean isNotAbleToRemove(LineSection lineSection) {
        return !isAbleToRemove(lineSection);
    }

    private boolean isAbleToRemove(LineSection lineSection) {
        LineSection lastLineSection = getLastLineSection()
            .orElse(null);

        return Objects.isNull(lastLineSection)
            || lastLineSection == lineSection;
    }

    public int size() {
        return lineSections.size();
    }

    public Stream<LineSection> stream() {
        return lineSections.stream();
    }

}
