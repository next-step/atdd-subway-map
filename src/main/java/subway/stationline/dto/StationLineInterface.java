package subway.stationline.dto;

public interface StationLineInterface {
    String name();
    String color();
    Long upStationId();
    Long downStationId();
    Long distance();
}
