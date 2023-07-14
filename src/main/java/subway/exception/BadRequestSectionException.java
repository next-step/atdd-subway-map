package subway.exception;

public class BadRequestSectionException extends RuntimeException {
    public static final String UP_STATION_ID_NOT_EQUALS_DOWN_STATION_ID_OF_LAST_SECTION =
            "UpStationId not equals downStationId of last section";
    public static final String DOWN_STATION_ID_IS_ALREADY_REGISTERED = "DownStationId is already registered";
    public static final String SECTION_IS_LAST = "Section is last";
    public static final String STATION_ID_IS_NOT_LAST = "StationId is not last";

    public BadRequestSectionException(String message) {
        super(message);
    }
}
