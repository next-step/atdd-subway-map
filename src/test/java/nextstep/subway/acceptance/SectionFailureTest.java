package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static nextstep.subway.acceptance.utils.DataUtils.subwayLineInit;

@DisplayName("지하철 구간 실패 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionFailureTest {

	@Autowired
	DatabaseCleanup databaseCleanup;

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
		subwayLineInit();
	}

	/**
	 * WHEN 이미 노선에 등록된 역을 구간에 등록하면
	 * THEN 오류가 발생한다.
	 */
	@DisplayName("중복역을 구간에 등록하면 오류가 발생한다")
	@Test
	void duplicateFailure() {
	    //given

	    //when

	    //then
	}

	/**
	 * WHEN 하행역이 아닌 역을 제거하려면
	 * THEN 오류가 발생한다.
	 */
	@DisplayName("하행역이 아닌 역을 제거하려면 오류가 발생한다")
	@Test
	void deleteFailureOnUpStation() {
	    //given

	    //when

	    //then
	}

	/**
	 * WHEN 구간이 1개인 경우에 역을 제거하려면
	 * THEN 오류가 발생한다.
	 */
	@DisplayName("구간이 1개인 경우에 역을 제거하려면 오류가 발생한다")
	@Test
	void deleteFailure() {
	    //given

	    //when

	    //then
	}
}
