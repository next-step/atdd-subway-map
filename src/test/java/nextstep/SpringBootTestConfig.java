package nextstep;


import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.domain.Section;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class SpringBootTestConfig {


    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }
}
