package nextstep.subway.applicaion.exception;

public class ExceptionMessages {

    private ExceptionMessages() {
    }

    public static String getNoLineExceptionMessage(long lineId) {
        return "노선이 존재하지 않습니다. lineId="+lineId;
    }

    public static String getNoStationExceptionMessage(long stationId) {
        return "지하철역이 존재하지 않습니다. stationId="+stationId;
    }

    public static String getNoEndpointInputExceptionMessage(long upStationId,long downEndpointStationId) {
        return "상행선의 입력값이 하행종점역이 아닙니다. upStationId = "+upStationId+", downEndpointStationId = "+downEndpointStationId;
    }
}
