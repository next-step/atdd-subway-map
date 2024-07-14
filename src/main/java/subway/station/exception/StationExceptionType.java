package subway.station.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StationExceptionType {

    INTERNAL_SERVER_ERROR("500", "Server Error"),
    STATION_NOT_FOUND("400", "Entity Not Found");

    private final String code;
    private final String message;
}
