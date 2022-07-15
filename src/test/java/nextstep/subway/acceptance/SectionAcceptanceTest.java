package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


@DisplayName("지하철 구간 인수테스트")
class SectionAcceptanceTest extends SpringBootTestConfig {

    /*
     * Given 2개의 지하철역과 하나의 노선을 생성하고, 지하철 구간에 추가한다.
     * When 지하철 구간을 등록한다.
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 구간을 등록한다")
    void 지하철_구간등록_테스트() {
        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Section 구간 = new Section(강남역.getId(), 선릉역.getId(), 10);

        //when
        ValidatableResponse 구간등록_응답 = sectionRestAssured.postRequest("/lines/1L/sections", 구간);

        //then
        구간등록_응답.statusCode(HttpStatus.CREATED.value());
    }


    /* Given 2개의 지하철역을 생성하고, 지하철 구간에 추가한다. 지하철 구간을 노선에 추가한다.
     * When 하행 종점의 지하철 구간을 삭제한다.
     * Then 하행 종점역이 비어있는지 확인한다.
     */




}
