package nextstep.subway.acceptance;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.JpaRepository;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
	static final String SIN_BOONDANG_LINE = "신분당선";
	static final String SAMSUNG_STATION = "삼성역";
	static final String LINE_COLOR_RED = "bg-red-600";
	static final String LINE_COLOR_BLUE = "bg-blue-600";

	@LocalServerPort
	int port;

	@Autowired
	List<JpaRepository> jpaRepositories;

	@BeforeEach
	void setup() {
		RestAssured.port = port;
		this.jpaRepositories.forEach(JpaRepository::deleteAllInBatch);
	}
}
