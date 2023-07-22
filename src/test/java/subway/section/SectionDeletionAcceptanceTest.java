package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
        var createSectionResponse = 지하철구간_등록();

        //when
        지하철구간_삭제(createSectionResponse);

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
        final long 노선의_첫번째_노선 = 2L;
        var createSectionResponse = 지하철구간_등록();

        //when
        var deleteResponse = 지하철구간_삭제(노선의_첫번째_노선);

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
        final long 노선의_첫번째_노선 = 2L;
        var deleteResponse = 지하철구간_삭제(노선의_첫번째_노선);

        //then
        예외_검증(deleteResponse, "제거하려는 지하철 노선의 구간이 1개인 경우 삭제할 수 없습니다.");
    }
}
