package subway.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static subway.section.fixture.SectionAssuredFixture.예외_검증;
import static subway.section.fixture.SectionAssuredFixture.지하철구간_검증;
import static subway.section.fixture.SectionRequestFixture.지하철구간_등록;
import static subway.section.fixture.SectionRequestFixture.지하철구간_조회;

@DisplayName("지하철구간 관리")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:data/section-manage-init.sql"})
public class SectionCreationAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void initPort(){
        RestAssured.port = port;
    }

    /**
     *  WHEN    지하철 구간을 등록하면
     *  THEN    지하철 구간을 조회하면 등록된 구간을 찾을 수 있다
     */
    @DisplayName("지하철구간 등록")
    @Test
    void createSection(){
        //when
        final Long 노선_하행역 = 2L;
        final Long 새로운_역 = 9L;
        var createResponse = 지하철구간_등록(노선_하행역, 새로운_역);

        //then
        var getResponse = 지하철구간_조회(createResponse);
        지하철구간_검증(getResponse, 노선_하행역, 새로운_역);
    }

    /**
     *  WHEN    노선의 하행 종착역이 새로운 지하철 구간의 상행성이 아니면
     *  THEN    예외가 발생한다
     */
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종착역이 아니면 예외")
    @Test
    void throwExceptionIfUpStationOfNewSectionIsNotDownStationOfLine(){
        //when
        final Long 새로운_하행역 = 9L;
        final Long 새로운_상행역 = 10L;
        var createSectionResponse = 지하철구간_등록(새로운_상행역, 새로운_하행역);

        //then
        예외_검증(createSectionResponse, "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종착역이어야 한다");
    }

    /**
     *  WHEN   노선에 존재하는 역을 새로운 지하철 구간의 하행선으로 등록하면
     *  THEN   예외가 발생한다
     */
    @DisplayName("새로운 구간의 하행역이 해당 노선에 등록되어있다면 예외")
    @Test
    void throwExceptionIfDownStationOfSectionIsAlreadyEnrolled(){
        //when
        final Long 노선_하행역 = 2L;
        final Long 이미_등록된_역 = 4L;
        var createSectionResponse = 지하철구간_등록(노선_하행역, 이미_등록된_역);

        //then
        예외_검증(createSectionResponse, "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다");
    }
}
