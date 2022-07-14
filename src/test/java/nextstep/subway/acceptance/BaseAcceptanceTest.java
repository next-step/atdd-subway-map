package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.init.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

public abstract class BaseAcceptanceTest {
	@LocalServerPort
	int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@BeforeEach
	public void commonSetUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
	}
}
