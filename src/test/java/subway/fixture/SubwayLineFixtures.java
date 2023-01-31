package subway.fixture;

import subway.presentation.request.SubwayLineRequest;

public class SubwayLineFixtures {
	/**
	 * SUB WAY LINE NAME
	 */
	public static final String SUBWAY_LINE_NAME_1 = "신분당선";
	public static final String SUBWAY_LINE_NAME_2 = "2호선";

	/**
	 * SUB WAY LINE COLOR
	 */
	public static final String SUBWAY_LINE_COLOR_1 = "bg-red-600";
	public static final String SUBWAY_LINE_COLOR_2 = "bg-red-300";

	/**
	 * SUB WAY LINE STATION ID
	 */
	public static final Long UP_STATION_ID_1 = 1L;
	public static final Long DOWN_STATION_ID_1 = 2L;
	public static final Long DOWN_STATION_ID_2 = 3L;

	/**
	 * SUB WAY LINE DISTANCE
	 */
	public static final int DISTANCE = 10;

	/**
	 * SUB WAY LINE CREATE REQUEST
	 * @return SubwayLineRequest.Create
	 */
	public static SubwayLineRequest.Create getSubwayLineRequest1() {
		return SubwayLineRequest.Create.builder()
			.name(SUBWAY_LINE_NAME_1)
			.color(SUBWAY_LINE_COLOR_1)
			.upStationId(UP_STATION_ID_1)
			.downStationId(DOWN_STATION_ID_1)
			.distance(DISTANCE)
			.build();
	}

	public static SubwayLineRequest.Create getSubwayLineRequest2() {
		return SubwayLineRequest.Create.builder()
			.name(SUBWAY_LINE_NAME_2)
			.color(SUBWAY_LINE_COLOR_2)
			.upStationId(UP_STATION_ID_1)
			.downStationId(DOWN_STATION_ID_2)
			.distance(DISTANCE)
			.build();
	}
}
