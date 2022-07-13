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

    public static String getNotEndpointInputExceptionMessage(long inputStationId,long downEndpointStationId) {
        return "입력값이 하행종점역이 아닙니다. inputStationId = "+inputStationId+", downEndpointStationId = "+downEndpointStationId;
    }

    public static String getNeedAtLeastOneSectionExceptionMessage() {
        return "구간이 1개남은 노선의 구간은 삭제할 수 없습니다.";
    }
}
