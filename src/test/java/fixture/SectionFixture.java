package fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationResponse;

public class SectionFixture {

    public Long upStationId;
    public Long downStationId;
    public Long distance;

    private SectionFixture(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionFixture 강남_선릉_구간() {
        var 강남역 = StationFixture.saveStationResponse("강남역").as(StationResponse.class);
        var 선릉역 = StationFixture.saveStationResponse("선릉역").as(StationResponse.class);
        return new SectionFixture(강남역.getId(), 선릉역.getId(), 10L);

    }

    public static ExtractableResponse<Response> save(Long lineId, SectionFixture fixture) {
        return RestAssured.given()
            .body(fixture)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{lineId}/sections", lineId)
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }
}
