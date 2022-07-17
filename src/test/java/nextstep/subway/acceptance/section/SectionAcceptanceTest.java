package nextstep.subway.acceptance.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.acceptance.AcceptanceTestBase;

@DisplayName("구간 관리 인수테스트")
class SectionAcceptanceTest extends AcceptanceTestBase {

	/*
	 * Given 1개의 지하철 노선을 생성하고
	 * When 해당 노선의 하행 종점역을 상행역으로 하는 새로운 구간을 추가하면
	 * Then 해당 노선의 하행 종점역이 새로 추가한 구간의 하행역으로 변경된다.
	 */
	@Test
	void 구간_추가_성공() {

	}

	/*
	 * Given 1개의 지하철 노선을 생성하고
	 * When 해당 노선의 하행 종점역을 상행역으로 하지 않는 새로운 구간을 추가하면
	 * Then 상행역 입력이 유효하지 않다는 예외가 발생한다.
	 */
	@Test
	void 구간_추가_시_유효하지_않은_상행역_입력_예외() {

	}

	/*
	 * Given 1개의 지하철 노선을 생성하고
	 * When 해당 노선의 상행 종점역을 하행역으로 하는 새로운 구간을 추가하면
	 * Then 하행역 입력이 유효하지 않다는 예외가 발생한다.
	 */
	@Test
	void 구간_추가_시_유효하지_않은_하행역_입력_예외() {

	}

	/*
	 * Given 1 개의 지하철 노선을 생성하고
	 * Given 1 개의 구간을 추가한 후,
	 * When 해당 노선의 하행 종점역을 하행역으로 하는 구간을 제거하면
	 * Then 해당 노선의 하행 종점역이 삭제한 구간의 상행역으로 변경된다.
	 */
	@Test
	void 구간_제거_성공() {

	}

	/*
	 * Given 1개의 지하철 노선을 생성하고
	 * When 해당 노선의 하행 종점역을 하행역으로 하는 구간을 제거하면
	 * Then 최소 구간 예외가 발생한다.
	 */
	@Test
	void 구간_제거_시_최소_구간_예외() {

	}

	/*
	 * Given 1 개의 지하철 노선을 생성하고
	 * Given 1 개의 구간을 추가한 후,
	 * When 해당 노선에 등록되지 않은 지하철역을 하행역으로하는 구간을 제거하면
	 * Then 노선에 등록되지 않은 구간 제거 예외가 발생한다.
	 */
	@Test
	void 구간_제거_시_노선에_등록되지_않은_구간_제거_예외() {

	}

	/*
	 * Given 1개의 지하철 노선을 생성하고
	 * Given 1 개의 구간을 추가한 후,
	 * When 해당 노선의 하행 종점역을 하행역으로 하지 않는 구간을 제거하면
	 * Then 유효하지 않은 하행역 입력 예외가 발생한다.
	 */
	@Test
	void 구간_제거_시_유효하지_않은_하행역_입력_예외() {

	}
}
