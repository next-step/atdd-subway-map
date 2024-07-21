package subway.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubwayExceptionType {

    INTERNAL_SERVER_ERROR("500", "Server Error"),
    STATION_NOT_FOUND("400", "Entity Not Found. stationId: %s"),
    LINE_NOT_FOUND("400", "Entity Not Found. lineId: %s"),

    INVALID_UP_STATION("400","Invalid Up Station. upStationId: %s"),
    INVALID_DOWN_STATION("400","Invalid Down Station. downStationId: %s"),

    CANNOT_DELETE_SINGLE_SECTION("400", "Cannot Delete Single Section"),
    CANNOT_DELETE_NON_LAST_DOWN_STATION("400", "Cannot Delete Non-Last Down Station");

    private final String code;
    private final String message;
}
