package subway.domain;

import org.springframework.stereotype.Component;
import subway.error.exception.BusinessException;
import subway.error.exception.ErrorCode;

@Component
public class SectionValidator {

    private static final int STATION_MIN_SIZE = 1;

    void validateSectionBeforeAdd(final Sections sections, final Station upStation, final Station downStation) {
        if (!sections.isLastDownStation(upStation)) {
            throw new BusinessException(ErrorCode.CANNOT_ADD_SECTION_WITH_INVALID_UP_STATION);
        }
        if (sections.isExistsStationInLine(downStation)) {

            throw new BusinessException(ErrorCode.CANNOT_ADD_SECTION_WITH_ALREADY_EXISTS_STATION_IN_LINE);
        }
    }

    void validateBeforeRemoveStation(final Sections sections, final Station station) {
        if (!sections.isExistsStationInLine(station)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_EXISTS_STATION_IN_LINE);
        }
        if (!sections.isLastDownStation(station)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_LAST_SECTION);
        }
        if (sections.getSize() <= STATION_MIN_SIZE) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_LAST_REMAINING_SECTION);
        }
    }
}
