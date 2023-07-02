package subway;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.path.json.JsonPath;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

	@DisplayName("지하철 노선을 생성한다")
	@Test
	void createStationLine() {
		//given
		final List<Long> stationIds = AcceptanceUtils.createStations(List.of("수유역", "강변역"));

		//when
		final Long lineId = AcceptanceUtils.createStationLine("4호선", "blue", stationIds.get(0), stationIds.get(1), BigDecimal.TEN);

		//then
		final List<Long> lineIds = AcceptanceUtils.getStationLines().getList("id", Long.class);

		Assertions.assertTrue(lineIds.contains(lineId));
	}

	/**
	 * 지하철 노선 목록 조회
	 **/
	@DisplayName("지하철 노선 목록 조회한다")
	@Test
	void getStationLines() {
		//given
		final List<Long> line1_stationIds = AcceptanceUtils.createStations(List.of("시청역", "서울역"));
		final List<Long> line2_stationIds = AcceptanceUtils.createStations(List.of("신촌역", "홍대입구역"));

		AcceptanceUtils.createStationLine("1호선", "blue", line1_stationIds.get(0), line1_stationIds.get(1), BigDecimal.ONE);
		AcceptanceUtils.createStationLine("2호선", "green", line2_stationIds.get(0), line2_stationIds.get(1), BigDecimal.TEN);

		//when
		final JsonPath jsonPath = AcceptanceUtils.getStationLines();

		//then
		final List<Long> lineIds = jsonPath.getList("id", Long.class);

		Assertions.assertEquals(2, lineIds.size());
	}

	/**
	 * 지하철 노선 조회
	 **/
	@DisplayName("지하철 노선 조회한다")
	@Test
	void getStationLine() {
		//given
		final List<Long> stationIds = AcceptanceUtils.createStations(List.of("수유역", "강변역"));
		final Long lineId = AcceptanceUtils.createStationLine("4호선", "blue", stationIds.get(0), stationIds.get(1), BigDecimal.TEN);

		//when
		final JsonPath jsonPath = AcceptanceUtils.getStationLine(lineId);

		//then
		Assertions.assertEquals("4호선", jsonPath.getString("name"));
		Assertions.assertEquals("blue", jsonPath.getString("color"));
		Assertions.assertEquals("수유역", jsonPath.getString("stations[0].name"));
		Assertions.assertEquals("강변역", jsonPath.getString("stations[1].name"));
	}

	@DisplayName("지하철 노선 수정")
	@Test
	void updateStationLine() {
		//given
		final List<Long> stationIds = AcceptanceUtils.createStations(List.of("수유역", "강변역"));
		final Long lineId = AcceptanceUtils.createStationLine("4호선", "blue", stationIds.get(0), stationIds.get(1), BigDecimal.TEN);

		//when
		AcceptanceUtils.updateStationLine(lineId, "9호선", "brown");

		//then
		final JsonPath jsonPath = AcceptanceUtils.getStationLine(lineId);
		Assertions.assertEquals("9호선", jsonPath.getString("name"));
		Assertions.assertEquals("brown", jsonPath.getString("color"));
	}

	@DisplayName("지하철 노선 삭제")
	@Test
	void deleteStationLine() {
		//given
		final List<Long> stationIds = AcceptanceUtils.createStations(List.of("수유역", "강변역"));
		final Long lineId = AcceptanceUtils.createStationLine("4호선", "blue", stationIds.get(0), stationIds.get(1), BigDecimal.TEN);

		//when
		AcceptanceUtils.deleteStationLine(lineId);

		//then
		final List<Long> lineIds = AcceptanceUtils.getStationLines().getList("id", Long.class);

		Assertions.assertFalse(lineIds.contains(lineId));
	}
}
