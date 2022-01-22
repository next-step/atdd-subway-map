package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

/*
* 테스트를 시 데이터 베이스 초기화를 위해 존재하는 클래스
* 직접 *Repository -> deleteAll()과 같이 해도 되지만, 이건 리포지토리가 변경 되면 같이 변경되기 때문에
* 사용하지 않는다. 물론 결국 DatabaseCleanup이 JPA에 의존하긴 하지만 특정 리포지토리에 의존하지는 않기 때문에
* 이 과정에서는 이렇게 한다.
* */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }
}
