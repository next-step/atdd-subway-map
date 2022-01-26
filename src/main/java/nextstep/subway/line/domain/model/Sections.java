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

    public Section add(Section section) {
        checkAddable(section.getUpStation(), section.getDownStation())
            .map(errorMessage -> new IllegalArgumentException(errorMessage.getMessage()))
            .ifPresent(e -> {
                throw e;
            });

        this.values.add(section);
        return section;
    }

    private Optional<ErrorMessage> checkAddable(Station upStation, Station downStation) {
        if (!values.isEmpty() && !existsDockingPoint(upStation)) {
            return Optional.of(ErrorMessage.NOT_FOUND_SECTION_DOCKING_POINT);
        }
        if (existsStation(downStation)) {
            return Optional.of(ErrorMessage.ALREADY_REGISTERED_STATION_IN_SECTION);
        }
        return Optional.empty();
    }

    private boolean existsDockingPoint(Station upStation) {
        return values.stream()
                     .anyMatch(iSection -> iSection.matchDownStation(upStation));
    }

    private boolean existsStation(Station station) {
        return values.stream()
                     .anyMatch(iSection -> iSection.matchUpStation(station) || iSection.matchDownStation(station));
    }

    public void delete(Long sectionId) {
        checkRemovable(sectionId)
            .map(errorMessage -> new IllegalArgumentException(errorMessage.getMessage()))
            .ifPresent(e -> {
                throw e;
            });

        values.removeIf(iSection -> iSection.matchId(sectionId));
    }

    private Optional<ErrorMessage> checkRemovable(Long sectionId) {
        if (values.size() <= 1) {
            return Optional.of(ErrorMessage.BELOW_MIN_SECTION_SIZE);
        }
        if (!matchLastSectionId(sectionId)) {
            return Optional.of(ErrorMessage.NON_LAST_SECTION);
        }
        return Optional.empty();
    }

    private boolean matchLastSectionId(Long sectionId) {
        Section lastSection = values.get(values.size() - 1);
        return lastSection.getId().equals(sectionId);
    }

    public List<Station> toStations() {
        return Stream.concat(values.stream().map(Section::getUpStation),
                             values.stream().map(Section::getDownStation))
                     .distinct()
                     .collect(Collectors.toList());
    }

    public int size() {
        return values.size();
    }
}
