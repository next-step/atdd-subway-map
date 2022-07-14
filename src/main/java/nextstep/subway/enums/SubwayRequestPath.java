package nextstep.subway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubwayRequestPath {
    STATION("/stations", "/{id}"),
    LINE("/lines", "/{id}");

    private final String value;
    private final String pathParam;

    public String addPathParam() {
        return value + pathParam;
    }
}
