package subway;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 구간 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationSectionAcceptanceTest {

	/**
	 * Given 지하철 역 A,C 와 해당역이 포함된 지하철 노선을 생성한다
	 * Given 지하철 역 B를 생성한다
	 * When 지하철 노선에 (C,B) 구간을 추가한다
	 * Then 지하철 노선 조회 시 추가한 구간의 역이 목록에 포함된다
	 * Then 지하철 노선의 하행 종점역은 새로 추가된 B역이 된다
	 */
	@DisplayName("지하철 구간 등록")
	@Test
	void createStationSection() {
		//given
		final Long aStationId = AcceptanceUtils.createStation("A역");
		final Long bStationId = AcceptanceUtils.createStation("B역");
		final Long cStationId = AcceptanceUtils.createStation("C역");
		final Long lineId = AcceptanceUtils.createStationLine("1호선", "blue", aStationId, cStationId, BigDecimal.TEN);

		//when
		AcceptanceUtils.createStationSection(lineId, cStationId, bStationId, BigDecimal.ONE, HttpStatus.OK);

		//then
		final List<String> stationNames = AcceptanceUtils.getStationLine(lineId).getList("stations.name", String.class);

		Assertions.assertEquals("A역", stationNames.get(0));
		Assertions.assertEquals("C역", stationNames.get(1));
		Assertions.assertEquals("B역", stationNames.get(2));
	}

	/**
	 * Given 지하철 역 A,B 와 해당역이 포함된 지하철 노선을 생성한다
	 * Given 지하철 역 C,D,E를 생성한다
	 * Given 지하철 노선에 (B,C), (C,D) 구간을 추가한다
	 * When 지하철 노선에 (A,E) 구간을 추가한다
	 * Then 에러 발생
	 */
	@DisplayName("새로운 구간의 상행역이 노선의 하행 종점역이 아닌 경우 구간 생성 시 에러")
	@Test
	void createStationSection_upStation_notEqual_lineLastDownStation() {
		//given
		final List<Long> stationIds = AcceptanceUtils.createStations(List.of("A역", "B역", "C역", "D역", "E역"));

		final Long aStationId = stationIds.get(0);
		final Long bStationId = stationIds.get(1);
		final Long cStationId = stationIds.get(2);
		final Long dStationId = stationIds.get(3);
		final Long eStationId = stationIds.get(4);

		final Long lineId = AcceptanceUtils.createStationLine("1호선", "blue", aStationId, bStationId, BigDecimal.TEN);

		AcceptanceUtils.createStationSection(lineId, bStationId, cStationId, BigDecimal.ONE, HttpStatus.OK);
		AcceptanceUtils.createStationSection(lineId, cStationId, dStationId, BigDecimal.ONE, HttpStatus.OK);

		//when & then
		AcceptanceUtils.createStationSection(lineId, aStationId, eStationId, BigDecimal.ONE, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 지하철 역 A,B 와 해당역이 포함된 지하철 노선을 생성한다
	 * Given 지하철 역 C를 생성한다
	 * Given 지하철 노선에 (B,C) 구간을 추가한다
	 * When 지하철 노선에 (A,C) 구간을 추가한다
	 * Then 에러 발생
	 */
	@DisplayName("새로운 구간의 하행역이 이미 노선에 등록된 역으로 구간생성 시 에러")
	@Test
	void createStationSection_sectionDownStation_registeredTo_lineStation() {
		//given
		final List<Long> stationIds = AcceptanceUtils.createStations(List.of("A역", "B역", "C역"));

		final Long aStationId = stationIds.get(0);
		final Long bStationId = stationIds.get(1);
		final Long cStationId = stationIds.get(2);

		final Long lineId = AcceptanceUtils.createStationLine("1호선", "blue", aStationId, bStationId, BigDecimal.TEN);

		//when
		AcceptanceUtils.createStationSection(lineId, bStationId, cStationId, BigDecimal.ONE, HttpStatus.OK);

		//then
		AcceptanceUtils.createStationSection(lineId, aStationId, cStationId, BigDecimal.ONE, HttpStatus.BAD_REQUEST);
	}
}
