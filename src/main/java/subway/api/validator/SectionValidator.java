package subway.api.validator;

import subway.domain.entity.Line;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.global.error.exception.InvalidValueException;

import static subway.global.error.exception.ErrorCode.*;

public class SectionValidator {

    public static void addSectionValidator(Line line, Station upStation, Station downStation) {
        if(line.getSections().size()>0){
            if (!line.getSections().get(line.getSections().size() - 1).getDownStation().equals(upStation)) {
                throw new InvalidValueException(STATION_NOT_FINAL);
            }
            for (Section section : line.getSections()) {
                if(section.getDownStation().equals(downStation)||section.getUpStation().equals(downStation)){
                    throw new InvalidValueException(ALREADY_REGISTERED_STAION);
                }
            }
        }
    }

    public static void deleteSectionValidator(Line line, Station station) {
        if (!line.getSections().get(line.getSections().size() - 1).getDownStation().equals(station)) {
            throw new InvalidValueException(STATION_NOT_FINAL);
        }
        if (line.getSections().size() < 2) {
            throw new InvalidValueException(STATION_LESS_THAN_TWO);
        }
    }
}
