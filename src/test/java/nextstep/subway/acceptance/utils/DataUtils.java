package nextstep.subway.acceptance.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.subway.acceptance.utils.StationUtils.지하철역을_등록한다;
import static nextstep.subway.acceptance.utils.SubwayLineUtils.지하철_노선을_등록한다;

public class DataUtils {

	public static final String LINE_NAME = "신분당선";
	public static final String LINE_COLOR = "bg-red-600";
	public static final String UP_STATION_NAME = "광교역";
	public static final String DOWN_STATION_NAME = "광교중앙역";
	public static final Integer DISTANCE = 5;

	public static long upStationId;
	public static long downStationId;
	public static long subwayLineId;
	public static ExtractableResponse<Response> subwayLineResponse;

	public static void subwayLineInit() {
		upStationId = 지하철역을_등록한다(UP_STATION_NAME).jsonPath().getLong("id");
		downStationId = 지하철역을_등록한다(DOWN_STATION_NAME).jsonPath().getLong("id");
		subwayLineResponse = 지하철_노선을_등록한다(LINE_NAME, LINE_COLOR, upStationId, downStationId, DISTANCE);
		subwayLineId = subwayLineResponse.jsonPath().getLong("id");
	}
}
