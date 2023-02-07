package subway.line.application;

import subway.exception.InvalidSectionDistanceException;
import subway.exception.InvalidSectionDownStationException;
import subway.exception.InvalidSectionUpStationException;
import subway.exception.SameEndStationException;
import subway.line.domain.Line;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;

import java.util.Objects;

public class LineValidator {
    public static boolean isValidate(
            Line line,
            Section section
    ) {
        Sections lineSections = line.getSections();
        if (lineSections.isEmpty()) {
            return true;
        }
        validateHasSameEndStation(section);
        validateSectionUpStation(line.getDownStation(), section.getUpStation());
        validateLineContainsSectionDownStation(lineSections, section.getDownStation());
        validateDistance(line.getDistance(), section.getDistance());
        return true;
    }

    private static void validateHasSameEndStation(Section section) {
        if (Objects.equals(section.getUpStation(), section.getDownStation())) {
            throw new SameEndStationException();
        }
    }

    private static void validateDistance(Long lineDistance, Long sectionDistance) {
        if (lineDistance < sectionDistance) {
            throw new InvalidSectionDistanceException();
        }
    }

    private static void validateSectionUpStation(
            Station lineDownStation,
            Station sectionUpStation) {
        if (!Objects.equals(lineDownStation.getId(), sectionUpStation.getId())) {
            throw new InvalidSectionUpStationException(sectionUpStation.getId());
        }
    }

    private static void validateLineContainsSectionDownStation(
            Sections lineSections, Station downStation) {
        if (lineSections.contains(downStation)) {
            throw new InvalidSectionDownStationException(downStation.getId());
        }
    }
}
