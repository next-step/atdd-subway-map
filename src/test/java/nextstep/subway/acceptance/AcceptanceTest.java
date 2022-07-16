package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.station.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        lineRepository.deleteAll();
        stationRepository.deleteAll();
    }
}
