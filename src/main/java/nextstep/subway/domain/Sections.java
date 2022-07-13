package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.exception.InvalidMatchEndStationException;
import nextstep.subway.domain.exception.StationAlreadyExistsException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    private static final int ONE = 1;
    private static final long EMPTY_VALUE = 0L;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> values = new ArrayList<>();

    public static Sections create() {
        return new Sections();
    }

    public void add(Section section) {
        Section lastSection = findLastSection();
        if (lastSection != null) {
            validateSection(lastSection, section);
        }

        this.values.add(section);
    }

    private void validateSection(Section lastSection, Section additionalSection) {
        if (!lastSection.isMatchDownStation(additionalSection.upStation())) {
            throw new InvalidMatchEndStationException(additionalSection.upStation().id());
        }
        if(this.hasStation(additionalSection.downStation())) {
            throw new StationAlreadyExistsException(additionalSection.downStation().id());
        }
    }

    private boolean hasStation(Station station) {
        return values.stream()
                .filter(section -> section.hasStation(station))
                .count() > EMPTY_VALUE;
    }

    private Section findLastSection() {
        if (lastIndex() < 0) {
            return null;
        }
        return values.get(lastIndex());
    }

    private int lastIndex() {
        return values.size() - ONE;
    }

    public List<Station> stations() {
        return values.stream()
                .map(Section::stations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }
}
