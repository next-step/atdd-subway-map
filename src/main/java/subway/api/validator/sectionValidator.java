package subway.api.validator;

import subway.domain.entity.Line;
import subway.domain.entity.Station;

public class sectionValidator {

    public static void addSectionValidator(Line line, Station upStation, Station downStation) {
        if(line.getSections().size()>0){
            if (!line.getSections().get(line.getSections().size() - 1).getDownStation().equals(upStation)) {
                throw new IllegalArgumentException();
            }
            line.getSections().forEach(section -> {
                if (section.getId().equals(downStation)) {
                    throw new IllegalArgumentException();
                }});
        }
    }

    public static void deleteSectionValidator(Line line, Station station) {
        if (!line.getSections().get(line.getSections().size() - 1).getDownStation().equals(station)) {
            throw new IllegalArgumentException();
        }
        if (line.getSections().size() < 2) {
            throw new IllegalArgumentException();
        }
    }
}
