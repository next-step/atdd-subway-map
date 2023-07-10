package fixture.then;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AbstractStringAssert;

public abstract class StationLineSectionThenFixture {

    public static AbstractStringAssert<?> 노선구간추가_상행역설정_오류_검사(ExtractableResponse<Response> response) {
        return assertThat(response.jsonPath().getString("message")).isEqualTo(
            "추가하고자 하는 구간의 상행역이, 노선의 하행종점역이 아닙니다.");
    }

    public static AbstractStringAssert<?> 노선구간추가_하행역이_이미존재할떄_오류_검사(ExtractableResponse<Response> response) {
        return assertThat(response.jsonPath().getString("message")).isEqualTo(
            "추가하고자 하는 구간의 하행역이 이미 구간에 존재합니다.");
    }
}
