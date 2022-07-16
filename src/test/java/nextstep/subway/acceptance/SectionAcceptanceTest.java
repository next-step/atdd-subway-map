package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tableTruncator")
public class SectionAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseTruncator databaseTruncator;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
        databaseTruncator.afterPropertiesSet();
    }

    @BeforeAll
    static void init(){

    }

    @AfterEach
    void tableClear(){
        databaseTruncator.cleanTable();
    }

    /*
     * When 지하철 노선에 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 등록한 지하철 구간을 확인할 수 있다
     * */
    @DisplayName("지하철 구간을 등록한다")
    @Test
    void registerSection(){

    }

    /*
     * Given 지하철 노선에 지하철 구간을 등록하고
     * when 등록한 지하설 구간을 삭제하면
     * Then 해당 지하철 구간은 삭제된다
     * */
    @DisplayName("지하철 구간을 삭제한다")
    @Test
    void deleteSection(){

    }

}

