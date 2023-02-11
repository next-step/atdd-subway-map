package subway.common;

import subway.exception.SubwayBadRequestException;
import subway.line.Line;
import subway.section.Section;
import subway.section.Sections;
import subway.station.Station;

public class LineSectionValidator {

    public static final int MIN_LINE_SECTION_SIZE = 1;

    public static void addValidate(Line line, Section section) {
        var sections = new Sections(line.getSections());
        validateLineLastStationMatches(sections, section.getUpStation());
        validateLineHasAlreadyDownStation(sections, section.getDownStation());
    }
    public static void deleteValidate(Sections sections, Station downStation) {
        validateLineSectionSize(sections);
        validateLineLastStationMatches(sections, downStation);
    }

    private static void validateLineSectionSize(Sections sections) {
        if (sections.getSize() <= MIN_LINE_SECTION_SIZE)
            throw new SubwayBadRequestException("can delete section when line has more than 2 section, current size: " + sections.getSize());
    }

    private static void validateLineLastStationMatches(Sections sections, Station station) {
        if (!sections.matchLastStation(station)) {
            throw new SubwayBadRequestException("station(" + station + ") is not downStation of the line");
        }
    }

    private static void validateLineHasAlreadyDownStation(Sections lineSection, Station downStation) {
        if (lineSection.contains(downStation)) {
            throw new SubwayBadRequestException("line already contains the station, " + downStation);
        }
    }
}
