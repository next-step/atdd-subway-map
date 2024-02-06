package fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.Line;
import subway.station.Station;
import subway.station.StationResponse;

public class LineFixture {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    private LineFixture(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Line giveOne(String name, String color, Station upStation, Station downStation, Long distance) {
        return Line.builder()
            .name(name)
            .color(color)
            .upStation(upStation)
            .downStation(downStation)
            .distance(distance)
            .build();
    }

    public static LineFixture 이호선() {
        var 서초역 = StationFixture.saveStationResponse("서초역").as(StationResponse.class);
        var 강남역 = StationFixture.saveStationResponse("강남역").as(StationResponse.class);

        return new LineFixture("이호선", "초록색", 서초역.getId(), 강남역.getId(), 10L);
    }

    public static ExtractableResponse<Response> save(LineFixture line) {
        return RestAssured.given().log().all()
            .body(line)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }
}
