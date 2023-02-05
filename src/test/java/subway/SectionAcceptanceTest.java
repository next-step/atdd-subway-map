package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@DisplayName("지하철구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @BeforeEach
    void setUp(){
        dataBaseCleanUp.excute();
    }
    /**
     * Given - 지하철 구간을 생성 요청을
     * When -
     * Then -
     */
    @DisplayName("지하철 구간을 생성한다. - 성공")
    @Test
    void createSectionTest_success() {

    }

    /**
     * Given -
     * When -
     * Then -
     */
    @DisplayName("지하철 구간을 생성한다. - 실패")
    @Test
    void createSectionTest_fail() {

    }

    /**
     * Given -
     * When -
     * Then -
     */
    @DisplayName("지하철 구간을 생성한다. - 성공")
    @Test
    void deleteSectionTest_success() {

    }

    /**
     * Given -
     * When -
     * Then -
     */
    @DisplayName("지하철 구간을 생성한다. - 실패")
    @Test
    void deleteSectionTest_fail() {

    }
}