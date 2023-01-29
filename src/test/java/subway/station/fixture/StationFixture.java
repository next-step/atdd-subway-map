package subway.station.fixture;

import java.util.HashMap;
import java.util.Map;

public enum StationFixture {
    연신내,
    충무로,
    교대,
    선릉;

    public Map toMap() {
        Map map = new HashMap();
        map.put("name", this.name());
        return map;
    }

}