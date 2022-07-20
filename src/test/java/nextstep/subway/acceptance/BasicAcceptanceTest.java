package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.common.LineRestAssured;
import nextstep.subway.common.SectionRestAssured;
import nextstep.subway.common.StationRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BasicAcceptanceTest {

  @InjectMocks
  StationRestAssured stationRestAssured;

  @InjectMocks
  LineRestAssured lineRestAssured;

  @InjectMocks
  SectionRestAssured sectionRestAssured;

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }
}
