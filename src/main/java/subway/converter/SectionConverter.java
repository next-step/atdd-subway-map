package subway.converter;

import subway.line.entity.Line;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.station.dto.StationResponse;

public class SectionConverter {

    private SectionConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SectionResponse convertToSectionResponseByLineAndSection(Line line, Section section) {

        return SectionResponse.of(line.getId(), section.getId(), new StationResponse(section.getUpStation().getId(), section.getUpStation().getName()),
                new StationResponse(section.getDownStation().getId(), section.getDownStation().getName()), section.getDistance());

    }
}
