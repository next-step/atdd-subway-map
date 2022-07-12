package nextstep.subway.applicaion.exception;

public class ExceptionMessages {

    private static final String NO_LINE_EXCEPTION_MESSAGE = "노선이 존재하지 않습니다.";
    private static final String NO_STATION_EXCEPTION_MESSAGE = "지하철역이 존재하지 않습니다.";

    private ExceptionMessages() {
    }

    public static String getNoLineExceptionMessage(long lineId) {
        return NO_LINE_EXCEPTION_MESSAGE+" lineId="+lineId;
    }

    public static String getNoStationExceptionMessage(long stationId) {
        return NO_STATION_EXCEPTION_MESSAGE+" stationId="+stationId;
    }
}
