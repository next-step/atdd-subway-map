package subway.common;

import subway.exception.SubwayBadRequestException;
import subway.line.Line;
import subway.section.Section;
import subway.section.Sections;
import subway.station.Station;

public class LineSectionValidator {
    public static void validate(Line line, Section section) {
        var lineSection = new Sections(line.getSections());

        validateLineLastStationMatchUpStation(lineSection, section.getUpStation());
        validateLineHasAlreadyDownStation(lineSection, section.getDownStation());
    }

    private static void validateLineLastStationMatchUpStation(Sections lineSection, Station upStation) {
        if (!lineSection.matchLastStation(upStation)) {
            throw new SubwayBadRequestException("upStation(" + upStation + ") must be same with downStation of line");
        }
    }

    private static void validateLineHasAlreadyDownStation(Sections lineSection, Station downStation) {
        if (lineSection.contains(downStation)) {
            throw new SubwayBadRequestException("line already contains the station, " + downStation);
        }
    }
}
