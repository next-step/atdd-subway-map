package subway.error;

public enum ErrorMessage {

    NO_DATA_LINE("해당 노선에 대한 정보가 없습니다."),
    NO_DATA_STATION("해당 역에 대한 정보가 없습니다."),

    NO_DATA_STATION_IN_LINE("노선에 속한 역에 대한 정보기 없습니다."),
    ;

    public final String message;

    ErrorMessage(String s) {
        message = s;
    }
}
