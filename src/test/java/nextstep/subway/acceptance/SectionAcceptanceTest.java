package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.provider.LineProvider.노선_상세_정보_조회됨;
import static nextstep.subway.provider.LineProvider.노선_생성됨;
import static nextstep.subway.provider.SectionProvider.*;
import static nextstep.subway.provider.StationProvider.지하쳘역_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    private Long 강남역_ID;
    private Long 삼성역_ID;
    private Long 역삼역_ID;
    private Long 노선_ID;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
        강남역_ID = 지하쳘역_등록됨("강남역");
        삼성역_ID = 지하쳘역_등록됨("삼성역");
        역삼역_ID = 지하쳘역_등록됨("역삼역");
        노선_ID = 노선_생성됨("신분당선", "bg-red-600", 강남역_ID, 삼성역_ID, 10);
    }

    /**
     * When 삼성역과 역삼역에 대한 구간 등록을 하면
     * Then 추가된 구간 정보에 따라 노선 정보 (downStationId, distance) 가 변경됩니다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createSection() throws Exception {
        // when
        final ExtractableResponse<Response> 지하철_구간_등록_응답 = 지하철_구간_생성_요청(노선_ID, 삼성역_ID, 역삼역_ID, 10);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철_구간_등록_응답, CREATED);
        assertThat(지하철_구간_등록_응답.jsonPath().getLong("upStationId")).isEqualTo(삼성역_ID);
        assertThat(지하철_구간_등록_응답.jsonPath().getLong("downStationId")).isEqualTo(역삼역_ID);
        assertThat(지하철_구간_등록_응답.jsonPath().getLong("distance")).isEqualTo(10);

        final ExtractableResponse<Response> 노선_상세_정보_조회_응답 = 노선_상세_정보_조회됨(노선_ID);
        final List<Object> 노선에_연결된_지하철역_목록 = 노선_상세_정보_조회_응답.jsonPath().getList("stations");
        final List<Object> 중복_제거가_된_지하철역_목록 = 노선에_연결된_지하철역_목록.stream().distinct().collect(Collectors.toList());
        assertThat(중복_제거가_된_지하철역_목록).hasSize(3);
    }

    /**
     * When 강남역과 역삼역에 대한 구간 등록을 하면
     * Then 새로운 구간의 상행역(강남역)과 기존 노선의 하행 종점역(삼성역)이 일치하지 않는 예외 처리가 발생합니다.
     */
    @DisplayName("지하철 구간 등록 시, 새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐 경우")
    @Test
    void createSectionErrorAboutUpStationAndDownStationIsNotMatched() throws Exception {
        // when
        final ExtractableResponse<Response> 지하철_구간_등록_응답 = 지하철_구간_생성_요청(노선_ID, 강남역_ID, 역삼역_ID, 10);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철_구간_등록_응답, BAD_REQUEST);
        assertThat(지하철_구간_등록_응답.jsonPath().getString("message"))
                .isEqualTo("새로운 구간의 상행역은 기존 노선의 하행 종점역이어야만 합니다.");
    }

    /**
     * When 삼성역과 강남역에 대한 구간 등록을 하면
     * Then 새로운 구간의 하행역(강남역)이 기존 노선에 이미 존재하는 예외 처리가 발생합니다.
     */
    @DisplayName("지하철 구간 등록 시, 새로운 구간의 하행역이 노선에 이미 등록되어있을 경우")
    @Test
    void createSectionErrorAboutNewDownStationIsAlreadyExists() throws Exception {
        // when
        final ExtractableResponse<Response> 지하철_구간_등록_응답 = 지하철_구간_생성_요청(노선_ID, 삼성역_ID, 강남역_ID, 10);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철_구간_등록_응답, BAD_REQUEST);
        assertThat(지하철_구간_등록_응답.jsonPath().getString("message"))
                .isEqualTo("이미 해당 노선의 구간에 등록되어있는 하행역입니다.");
    }

    /**
     * When 삼성역과 역삼역에 대한 구간이 생성되고, 역삼역을 구간에서 삭제하면
     * Then 지하철 노선에 대한 구간 목록에서 해당 구간이 조회되지 않습니다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSection() throws Exception {
        // when
        final Long 생성된_하행_종점역_ID = 지하철_구간_생성됨(노선_ID, 삼성역_ID, 역삼역_ID, 10);
        final ExtractableResponse<Response> 지하철_구간_삭제_응답 = 지하철_구간_삭제_요청(노선_ID, 생성된_하행_종점역_ID);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철_구간_삭제_응답, NO_CONTENT);

        final ExtractableResponse<Response> 노선_상세_정보_조회_응답 = 노선_상세_정보_조회됨(노선_ID);
        final List<Object> 노선에_연결된_지하철역_목록 = 노선_상세_정보_조회_응답.jsonPath().getList("stations");
        assertThat(노선에_연결된_지하철역_목록).hasSize(2);
    }

    /**
     * When 삼성역과 역삼역에 대한 구간이 생성되고, 삼성역을 구간에서 삭제하면
     * Then 삼성역이 하행역인 구간이 모든 구간 중, 마지막 구간(삼성역 - 역삼역)이 아니라는 예외 처리가 발생합니다.
     */
    @DisplayName("지하철 구간 삭제 시, 삭제하려는 구간이 마지막 구간이 아닐 경우")
    @Test
    void deleteSectionErrorAboutIsNotALastSection() throws Exception {
        // when
        지하철_구간_생성됨(노선_ID, 삼성역_ID, 역삼역_ID, 10);
        final ExtractableResponse<Response> 지하철_구간_삭제_응답 = 지하철_구간_삭제_요청(노선_ID, 삼성역_ID);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철_구간_삭제_응답, BAD_REQUEST);
        assertThat(지하철_구간_삭제_응답.jsonPath().getString("message"))
                .isEqualTo("마지막 구간만 삭제할 수 있습니다.");
    }

    /**
     * When 삼성역을 구간에서 삭제하면
     * Then 삭제하려는 노선의 구간이 하나 밖에 남지 않았다는 예외 처리가 발생합니다.
     */
    @DisplayName("지하철 구간 삭제 시, 노선에 남은 구간이 한 개일 경우")
    @Test
    void deleteSectionErrorAboutRemainingSectionCountIsOne() throws Exception {
        // when
        final ExtractableResponse<Response> 지하철_구간_삭제_응답 = 지하철_구간_삭제_요청(노선_ID, 삼성역_ID);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철_구간_삭제_응답, BAD_REQUEST);
        assertThat(지하철_구간_삭제_응답.jsonPath().getString("message"))
                .isEqualTo("남은 구간이 한 개일 경우, 삭제할 수 없습니다.");
    }
}
