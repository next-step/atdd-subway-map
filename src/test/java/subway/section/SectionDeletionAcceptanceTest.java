package subway.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static subway.section.fixture.SectionAssuredFixture.삭제된_구간_검증;
import static subway.section.fixture.SectionAssuredFixture.예외_검증;
import static subway.section.fixture.SectionRequestFixture.*;

@DisplayName("지하철구간 삭제 관리")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:data/section-manage-init.sql"})
public class SectionDeletionAcceptanceTest {

    @LocalServerPort
    private int port;

    final Long 신분당선 = 1L;
    final Long 판교역 = 2L;
    final Long 정자역 = 6L;
    final Long 미금역 = 11L;


    @BeforeEach
    public void initPort(){
        RestAssured.port = port;
    }

    /**
     *  GIVEN   지하철 구간 등록
     *  WHEN    지하철 구간 삭제
     *  WHEN    생성했던 구간 조회
     *  THEN    구간 조회 실패 확인
     */
    @DisplayName("지하철구간 삭제")
    @Test
    void deleteSection(){
        //given
        var createSectionResponse = 지하철구간_등록(신분당선, 판교역, 정자역);

        //when
        지하철구간_삭제(신분당선, createSectionResponse);

        //when
        var getResponse = 지하철구간_조회(createSectionResponse);

        //then
        삭제된_구간_검증(getResponse);
    }

    /**
     *  GIVEN   지하철 구간 등록
     *  WHEN    노선의 마지막이 아닌 구간 삭제
     *  THEN    예외 발생 확인
     */
    @DisplayName("제거하는 지하철 구간이 노선의 마지막 구간이 아닐 경우 예외")
    @Test
    void throwExceptionWhenDeletingNotLastSection(){
        //given
        var firstSection = 지하철구간_등록(신분당선, 판교역, 정자역);
        var secondSection = 지하철구간_등록(신분당선, 정자역, 미금역);

        //when
        var deleteResponse = 지하철구간_삭제(신분당선, firstSection);

        //then
        예외_검증(deleteResponse, "제거하는 지하철 구간이 노선의 마지막 구간이 아닐 경우 삭제할 수 없습니다.");
    }

    /**
     *  WHEN    노선에 하나밖에 없는 구간 삭제
     *  THEN    예외 발생 확인
     */
    @DisplayName("제거하려는 지하철 노선의 구간이 1개인 경우 예외")
    @Test
    void throwExceptionWhenLineHasOnlyOneSection(){
        //when
        var deleteResponse = 지하철구간_삭제(신분당선, 판교역);

        //then
        예외_검증(deleteResponse, "제거하려는 지하철 노선의 구간이 1개인 경우 삭제할 수 없습니다.");
    }
}
