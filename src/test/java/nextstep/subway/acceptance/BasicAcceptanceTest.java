package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.common.LineRestAssured;
import nextstep.subway.common.StationRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BasicAcceptanceTest {

  final StationRestAssured stationRestAssured = new StationRestAssured();
  final LineRestAssured lineRestAssured = new LineRestAssured();

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }
}
