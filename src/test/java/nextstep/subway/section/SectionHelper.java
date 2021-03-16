package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionHelper {

    public static SectionRequest Section_요청_만들기(Long upStationId, Long downStationId, int distance){
        return new SectionRequest(upStationId, downStationId, distance);

    }

    public static ExtractableResponse<Response> 구간_추가_요청(Long lineId, Long upStationId, Long downStationId, int distance){
        return RestAssured.given().pathParam("lineId", lineId)
                .body(Section_요청_만들기(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().post("/lines/{lineId}/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 구간_삭제(Long lineId, Long stationId){
        ExtractableResponse<Response> deleteResponse = RestAssured.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", lineId)
                .queryParam("stationId", stationId)
                .log().all()
                .when().delete("/lines/{lineId}/sections")
                .then().log().all().extract();
        return deleteResponse;
    }


    public static void 응답_204_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 응답_200_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 응답_400_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 응답_201_확인(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
