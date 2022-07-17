package nextstep.subway.acceptance.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import nextstep.subway.acceptance.isolation.TestIsolationUtil;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTestBase {
	@LocalServerPort
	int port;

	@Autowired
	TestIsolationUtil testIsolationUtil;

	@BeforeEach
	protected void setUp() {
		RestAssured.port = port;
		testIsolationUtil.clean();
	}
}
