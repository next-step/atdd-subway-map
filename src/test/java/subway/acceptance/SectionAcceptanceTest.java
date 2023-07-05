package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class SectionAcceptanceTest {

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 입력 구간의 상행역이 등록하려는 노선의 하행 종점역이 아니면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록시 상행선 오류")
    @Test
    void createSectionWithUpStationException() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        assertThat(sectionUpStationId).isNotEqualTo(downStationId);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.createWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("error.message")).isEqualTo("신규 구간의 상행역은 노선의 하행 종점역이어야 합니다.")
        );
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 입력 구간의 하행역이 등록하려는 노선에 존재하는 역이면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록시 하행선 오류")
    @Test
    void createSectionWithDownStationException() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        // and
        Long sectionUpStationId = 3L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        assertThat(line.isContains(downStationId)).isTrue();

        //when then
        ExtractableResponse<Response> response = RestAssuredUtil.createWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("error.message")).isEqualTo("신규 구간의 하행역은 기존 노선에 포함되면 안됩니다.")
        );
    }

    /**
     * given 하행역ID, 상행역ID, 구간거리를 입력하고
     * and 등록하려는 노선이 있고
     * and 입력 구간의 하행역이 등록하려는 노선에 존재하는 역이면
     * when 노선에 입력 구간을 등록하려고 할 때
     * then 등록 불가 합니다.
     */
    @DisplayName("구간 등록")
    @Test
    void createSection() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        // and
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        // and
        Long sectionUpStationId = 1L;
        Long sectionDownStationId = 4L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        assertThat(sectionUpStationId).isEqualTo(downStationId);

        // and
        assertThat(line.isContains(downStationId)).isFalse();

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.createWithCreated(
            "/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.jsonPath().getLong("id")).isNotNull(),
            () -> assertThat(response.jsonPath().getLong("downStation.id")).isEqualTo(sectionDownStationId),
            () -> assertThat(response.jsonPath().getLong("upStation.id")).isEqualTo(sectionUpStationId),
            () -> assertThat(response.jsonPath().getLong("distance")).isEqualTo(sectionDistance)
        );
    }

    /**
     * given 2개 이상의 구간을 포함하는 노선과 삭제하려는 역ID가 있을때
     * and 삭제하려는 역이 해당 노선에 존재하는 역이 아니면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_미존재 역")
    @Test
    void deleteWithNotContainedStation() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        Long sectionUpStationId = downStationId;
        Long sectionDownStationId = 3L;
        Long sectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", sectionDownStationId,
            "upStationId", sectionUpStationId,
            "distance", sectionDistance);

        RestAssuredUtil.createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        Long deleteStationId = 4L;

        // and
        assertThat(line.isContains(deleteStationId)).isFalse();

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections?stationsId=", deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("error.message")).isEqualTo("구간에 포함되지 않는 역은 삭제할 수 없습니다.")
        );
    }

    /**
     * given 2개 이상의 구간을 포함하는 노선과 삭제하려는 역ID가 있을때
     * and 삭제하려는 역이 해당 노선의 하행종착역이 아니면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_하행 종착역 아님")
    @Test
    void deleteWithNotDownEndStation() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        Long newSectionUpStationId = downStationId;
        Long newSectionDownStationId = 3L;
        Long newSectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", newSectionDownStationId,
            "upStationId", newSectionUpStationId,
            "distance", newSectionDistance);

        RestAssuredUtil.createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        Long deleteStationId = 4L;

        // and
        assertThat(deleteStationId).isNotEqualTo(line.downEndStation().id());

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections?stationsId=", deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("error.message")).isEqualTo("노선의 하행 종착역만 삭제 가능합니다.")
        );
    }

    /**
     * given 노선과 삭제하려는 역ID가 있고
     * and 역이 노선에 포함되고
     * and 노선에 구간이 하나만 존재하면
     * when 노선에서 구간을 삭제하려 할때
     * then 삭제 불가합니다.
     */
    @DisplayName("구간 제거_하행 종착역 아님")
    @Test
    void deleteStationFromLineContainingOnlyOneSection() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        Long newSectionUpStationId = downStationId;
        Long newSectionDownStationId = 3L;
        Long newSectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", newSectionDownStationId,
            "upStationId", newSectionUpStationId,
            "distance", newSectionDistance);

        RestAssuredUtil.createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        Long deleteStationId = 4L;

        // and
        assertThat(line.isContains(deleteStationId)).isTrue();

        // and
        assertThat(line.isContainingOnlyOneSection()).isTrue();

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithBadRequest(
            "/lines/" + line.jsonPath().getLong("id") + "/sections?stationsId=", deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("error.message")).isEqualTo("노선에 구간이 1개만 존재해 역을 삭제할 수 없습니다.")
        );
    }

    /**
     * given 노선과 삭제하려는 역ID가 있고
     * and 삭제하려는 역이 노선의 하행 종착역이고
     * and 노선에 구간이 2개 이상이면
     * when 노선에서 구간을 삭제하려 할때
     * then 정상 삭제됩니다.
     */
    @DisplayName("구간 제거")
    @Test
    void deleteSectionFromLine() {
        //given
        Long downStationId = 1L;
        Long upStationId = 2L;
        Long distance = 10L;
        String name = "신분당선";
        String color = "bg-red-600";
        Map<String, Object> map = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );
        ExtractableResponse<Response> line = RestAssuredUtil.createWithCreated("/lines",
            map);

        Long newSectionUpStationId = downStationId;
        Long newSectionDownStationId = 3L;
        Long newSectionDistance = 5L;
        Map<String, Object> param = Map.of("downStationId", newSectionDownStationId,
            "upStationId", newSectionUpStationId,
            "distance", newSectionDistance);

        RestAssuredUtil.createWithCreated("/lines/" + line.jsonPath().getLong("id") + "/sections"
            , param);

        // and
        Long deleteStationId = newSectionDownStationId;

        // and
        assertThat(deleteStationId).isEqualTo(line.downEndStation().id());

        // and
        assertThat(line.hasMoreThanTwoSections()).isTrue();

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.deleteWithNoContent(
            "/lines/" + line.jsonPath().getLong("id") + "/sections?stationsId=", deleteStationId);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
            () -> assertThat(response.body().asString()).isBlank()
        );
    }
}
