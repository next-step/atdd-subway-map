package nextstep.subway.line.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.common.exception.InvalidArgumentException;
import nextstep.subway.common.exception.OptionalException;
import nextstep.subway.station.domain.model.Station;

@Embeddable
public class Sections {
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "line",
        cascade = { CascadeType.PERSIST, CascadeType.MERGE },
        orphanRemoval = true
    )
    private List<Section> values;

    public Sections() {
        values = new ArrayList<>();
    }

    public Section add(Line line, Station upStation, Station downStation, Distance distance) {
        verifySectionDockingPoint(upStation).verify();
        verifyAlreadyRegisteredStationInSection(downStation).verify();

        Section section = Section.builder()
            .line(line)
            .upStation(upStation)
            .downStation(downStation)
            .distance(distance)
            .build();
        this.values.add(section);
        return section;
    }

    private OptionalException<InvalidArgumentException> verifySectionDockingPoint(Station upStation) {
        if (values.isEmpty()) {
            return OptionalException.empty();
        }
        Optional<Section> optionalSection =
            values.stream()
                  .filter(iSection -> iSection.matchDownStation(upStation))
                  .findFirst();
        return OptionalException.ifEmpty(
            optionalSection,
            () -> new InvalidArgumentException(ErrorMessage.NOT_FOUND_SECTION_DOCKING_POINT.getMessage())
        );
    }

    private OptionalException<InvalidArgumentException> verifyAlreadyRegisteredStationInSection(Station downStation) {
        Optional<Section> optionalSection =
            values.stream()
                  .filter(iSection -> iSection.matchUpStation(downStation) || iSection.matchDownStation(downStation))
                  .findFirst();
        return OptionalException.ifPresent(
            optionalSection,
            () -> new InvalidArgumentException(ErrorMessage.ALREADY_REGISTERED_STATION_IN_SECTION.getMessage())
        );
    }

    public void delete(Long sectionId) {
        verifySectionSize().verify();
        verifyNonLastSection(sectionId).verify();

        values.removeIf(iSection -> iSection.matchId(sectionId));
    }

    private OptionalException<InvalidArgumentException> verifySectionSize() {
        if (values.size() <= 1) {
            return OptionalException.of(
                new InvalidArgumentException(ErrorMessage.BELOW_MIN_SECTION_SIZE.getMessage())
            );
        }
        return OptionalException.empty();
    }

    private OptionalException<InvalidArgumentException> verifyNonLastSection(Long sectionId) {
        Section lastSection = values.get(values.size() - 1);
        if (lastSection.getId().equals(sectionId)) {
            return OptionalException.empty();
        }
        return OptionalException.of(
            new InvalidArgumentException(ErrorMessage.NON_LAST_SECTION.getMessage())
        );
    }

    public List<Station> toStations() {
        return Stream.concat(values.stream().map(Section::getUpStation),
                             values.stream().map(Section::getDownStation)
        )
                     .distinct()
                     .sorted()
                     .collect(Collectors.toList());
    }

    public int size() {
        return values.size();
    }
}
