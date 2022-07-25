package nextstep.subway;

public interface TestDataSupplier {
    String KEY_ID = "id";
    String KEY_NAME = "name";
    String KEY_COLOR = "color";
    String KEY_UP_STATION_ID = "upStationId";
    String KEY_DOWN_STATION_ID = "downStationId";
    String KEY_DISTANCE = "distance";

    String GANGNAM_STATION = "강남역";
    String SINDORIM_STATION = "신도림역";
    String PANGYO_STATION = "판교역";

    String LINE1_NAME = "1호선";
    String LINE1_COLOR = "red";
    Long LINE1_UP_STATION_ID = 1L;
    Long LINE1_DOWN_STATION_ID = 3L;
    Long LINE1_DISTANCE = 10L;

    String LINE2_NAME = "2호선";
    Long LINE2_UP_STATION_ID = 2L;
    String LINE2_COLOR = "blue";
    Long LINE2_DOWN_STATION_ID = 4L;
    Long LINE2_DISTANCE = 20L;
}
