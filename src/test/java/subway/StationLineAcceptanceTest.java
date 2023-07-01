package subway;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

	@DisplayName("지하철 노선을 생성한다")
	@Test
	void createStationLine() {
		//given
		final List<Long> stationIds = AcceptanceUtils.createStations(List.of("수유역", "강변역"));

		//when
		final Long stationLineId = AcceptanceUtils.createStationLine("노원역", "blue", stationIds.get(0), stationIds.get(1), BigDecimal.TEN);

		//then
		final List<Long> lineIds = AcceptanceUtils.getStationLines().getList("id", Long.class);

		Assertions.assertTrue(lineIds.contains(stationLineId));
	}

	/**
	 * 지하철 노선 목록 조회
	 **/
	@DisplayName("지하철 노선 목록 조회한다")
	@Test
	void getStationLines() {

	}

	/**
	 * 지하철 노선 조회
	 **/
	@DisplayName("지하철 노선 조회한다")
	@Test
	void getStationLine() {

	}

	/**
	 * 지하철 노선 수정
	 * Given 지하철 노선도를 생성한다
	 * When 생성한 지하철 노선도의 색과 이름 정보를 수정요청한다
	 * Then 지하철 노선도 조회하면 수정된 색과 이름 정보를 응답한다
	 **/
	@DisplayName("지하철 노선 수정")
	@Test
	void updateStationLine() {

	}

	/**
	 * 지하철 노선 삭제
	 * Given 지하철 노선도를 생성한다
	 * When 생성된 지하철 노선도를 삭제요청한다
	 * Then 지하철 노선도 목록 요청에서 해당 지하철 노선도를 찾을수 없다
	 **/
	@DisplayName("지하철 노선 삭제")
	@Test
	void deleteStationLine() {

	}
}
