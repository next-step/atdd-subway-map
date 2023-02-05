package subway.common.fixture;

import java.util.HashMap;
import java.util.Map;

import static subway.common.fixture.FieldFixture.노선_간_거리;
import static subway.common.fixture.FieldFixture.노선_상행_종점역_ID;
import static subway.common.fixture.FieldFixture.노선_하행_종점역_ID;

public enum SectionFixture {

    강남역_구간("7"),
    역삼역_구간("4"),
    선릉역_구간("8"),
    ;

    private final String distance;

    SectionFixture(String distance) {
        this.distance = distance;
    }

    public String 노선_간_거리() {
        return distance;
    }

    public Map<String, String> 요청_데이터_생성(String 상행역_id, String 하행역_id) {
        Map<String, String> params = new HashMap<>();
        params.put(노선_상행_종점역_ID.필드명(), 상행역_id);
        params.put(노선_하행_종점역_ID.필드명(), 하행역_id);
        params.put(노선_간_거리.필드명(), 노선_간_거리());
        return params;
    }
}
