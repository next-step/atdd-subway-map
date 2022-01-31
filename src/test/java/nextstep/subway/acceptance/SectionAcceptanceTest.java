package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest{

	/**
	 * Scenario
	 * 구간 등록 기능
	 *
	 * When
	 * 구간 등록 요청을 하면
	 *
	 * Then
	 * 노선의 하행에 구간이 등록된다
	 *
	 * Condition
	 * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
	 * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
	 * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
	 */
	@DisplayName("구간 등록")
	@Test
	void createSection() {
	}
	
	/**
	 * Scenario
	 * 구간 제거 기능
	 *
	 * When
	 * 구간 제거 요청을 하면
	 *
	 * Then
	 * 구간이 제거된다
	 *
	 * Condition
	 * 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
	 * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
	 * 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
	 */
	@DisplayName("구간 삭제")
	@Test
	void deleteSection() {
	}

	/**
	 * Scenario
	 * 등록된 구간을 통해 역 목록 조회 기능
	 *
	 * When
	 * 구간 조회를 요청하면
	 *
	 * Then
	 * 구간에 등록된 역이 조회된다
	 *
	 * Condition
	 * 지하철 노선 조회 시 등록된 역 목록을 함께 응답
	 * 노선에 등록된 구간을 순서대로 정렬하여 상행 종점부터 하행 종점까지 목록을 응답하기
	 */
	@Test
	void findStationsBySection() {
	}
}
