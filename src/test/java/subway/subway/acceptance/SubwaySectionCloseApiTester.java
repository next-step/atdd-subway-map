package subway.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import subway.subway.application.query.StationResponse;
import subway.subway.application.query.SubwayLineResponse;

@Component
public class SubwaySectionCloseApiTester extends ApiTester{

    private final String URL = "/subway-lines/{subway-line-id}/sections";

    private ExtractableResponse<Response> request(Long subwayLineId,Long stationId) {

        RequestBuilder param = new RequestBuilder()
                .add("stationId", stationId);

        return RestAssured
                .given().log().all()
                .params(param.build())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(URL, subwayLineId)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 노선에_구간을_삭제한다(SubwayLineResponse subwayLine, StationResponse station) {
        Long subwayLineId = subwayLine.getId();
        Long stationId = station.getId();

        return request(subwayLineId, stationId);
    }

    public void 삭제하는_구간의_하행역이_하행_종점역이_아니면_에러_발생(ExtractableResponse<Response> response) {

        Assertions.assertThat(response.statusCode()).isEqualTo(400);

    }

    public void 구간이_하나뿐인_노선은_삭제_시도_시에_에러_발생(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(400);

    }

}
