package subway.common.fixture;

import java.util.HashMap;
import java.util.Map;

import static subway.common.fixture.FieldFixture.노선_간_거리;
import static subway.common.fixture.FieldFixture.노선_상행_종점역_ID;
import static subway.common.fixture.FieldFixture.노선_색깔;
import static subway.common.fixture.FieldFixture.노선_이름;
import static subway.common.fixture.FieldFixture.노선_하행_종점역_ID;

public enum LineFixture {
    이호선("2호선", "bg-green-600", "10", null, null),
    사호선("4호선", "bg-blue-600", "20", null, null),
    ;

    private final String name;
    private final String color;
    private final String distance;
    private final String upStationId;
    private final String downStationId;

    LineFixture(String name, String color, String distance, String upStationId, String downStationId) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public String 노선_이름() {
        return name;
    }

    public String 노선_색깔() {
        return color;
    }

    public String 노선_간_거리() {
        return distance;
    }

    public String 상행종점역_아이디() {
        return upStationId;
    }

    public String 하행종점역_아이디() {
        return downStationId;
    }

    public Map<String, String> 생성_요청_데이터_생성(String 상행종점역_id, String 하행종점역_id) {
        Map<String, String> params = new HashMap<>();
        params.put(노선_이름.필드명(), 노선_이름());
        params.put(노선_색깔.필드명(), 노선_색깔());
        params.put(노선_간_거리.필드명(), 노선_간_거리());
        params.put(노선_상행_종점역_ID.필드명(), 상행종점역_id);
        params.put(노선_하행_종점역_ID.필드명(), 하행종점역_id);
        return params;
    }

    public Map<String, String> 수정_요청_데이터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put(노선_이름.필드명(), 노선_이름());
        params.put(노선_색깔.필드명(), 노선_색깔());
        return params;
    }
}
