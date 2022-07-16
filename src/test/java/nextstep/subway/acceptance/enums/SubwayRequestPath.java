package nextstep.subway.acceptance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
@Getter
public enum SubwayRequestPath {
    STATION("/stations", "/{id}"),
    LINE("/lines", "/{id}"),

    SECTION("/sections", "/{id}");

    private final String value;
    private final String pathParam;

    public String sectionsRequestPath(Long id) {
        return LINE.getValue() + File.separator + id + SECTION.getValue();
    }
}
