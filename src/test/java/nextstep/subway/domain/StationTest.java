package nextstep.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철_역_관련_단위_테스트")
@SpringBootTest
public class StationTest {

	/**
	 * given 2개의 지하철 역을 생성한 후
	 * when 하나의 지하철 역을 다른 지하철 역의 구간에 추가하면
	 * then 지하철 역 구간에 해당 지하철 역이 들어간다.
	 */
	@DisplayName("지하철 구간 추가 단위 테스트")
	@Test
	void addSection() {
		// given
		Station upStation = new Station("강남역");
		Station downStation = new Station("양재역");
		// when
		upStation.addSection(downStation, 10);
		// then
		Assertions.assertThat(upStation.getSection().getNextStation())
				.isEqualTo(downStation);
	}
}
