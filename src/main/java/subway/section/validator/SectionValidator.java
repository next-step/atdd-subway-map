package subway.section.validator;

import subway.line.entity.Line;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.exception.SectionException;
import subway.station.entity.Station;

import java.util.List;

import static subway.common.constant.ErrorCode.*;
import static subway.converter.LineConverter.convertToStationIds;

public class SectionValidator {

    private SectionValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validateCreateSection(Line line, Station upStation, Station downStation) {
        List<Long> sectionsStation = convertToStationIds(line);

        validateSectionsLastDownStationIdIsSameUpStationId(line.getSections().getLastStation().getId(), upStation.getId());
        validateSectionDoesNotContainStation(sectionsStation, downStation.getId());
    }

    public static void validateSectionsLastDownStationIdIsSameUpStationId(Long sectionsLastDownStationId, Long upStationId) {
        if (sectionsLastDownStationId != upStationId) {
            throw new SectionException(String.valueOf(SECTION_NOT_MATCH));
        }
    }

    public static void validateSectionDoesNotContainStation(List<Long> sectionsStation, Long downStationId) {
        if (sectionsStation.contains(downStationId)) {
            throw new SectionException("This descending station already exists. ");
        }
    }

    public static void validateDeleteSection(Sections sections, Long stationId) {
        validateSectionsLastDownStationIsNot(sections.getLastStation().getId(), stationId);
        validateSectionsMinimumCount(sections.getSections());
    }

    public static void validateSectionsLastDownStationIsNot(Long sectionsLastDownStationId, Long stationId) {
        if (sectionsLastDownStationId != stationId) {
            throw new SectionException(String.valueOf(SECTION_NOT_PERMISSION_NOT_LAST_DESCENDING_STATION));
        }
    }

    public static void validateSectionsMinimumCount(List<Section> sections) {
        if (sections.size() <= 1) {
            throw new SectionException(String.valueOf(SECTION_NOT_PERMISSION_COUNT_TOO_LOW));
        }
    }
}
