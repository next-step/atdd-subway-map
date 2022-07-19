package nextstep.subway.acceptance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubwayTestHelper {

    static RestUtil restUtil = new RestUtil();

    public List<Map<String, Object>> makeStationList(String... names) {
        List<Map<String, Object>> lists = new ArrayList<>();
        for (String name : names) {
            lists.add(Map.of("name", name));
        }
        return lists;
    }

    public List<Map<String, Object>> makeLineList(List<Object>... params) {
        List<Map<String, Object>> lists = new ArrayList<>();
        for (List<Object> param : params) {
            lists.add(Map.of("name", param.get(0).toString(),
                    "color", param.get(1).toString(),
                    "upStationId", param.get(2),
                    "downStationId", param.get(3),
                    "distance", param.get(4)));
        }
        return lists;
    }

    public List<Map<String ,Object>> makeSectionList(List<Object>... params){
        List<Map<String, Object>> lists = new ArrayList<>();
        for (List<Object> param : params) {
            lists.add(Map.of(
                    "upStationId", param.get(0),
                    "downStationId", param.get(1),
                    "distance", param.get(2)));
        }
        return lists;
    }

    public void makeGivenCondition(String requestUrl, int expectHttpStatusCode, Map<String, Object>... params) {
        for (Map<String, Object> param : params) {
            restUtil.createEntityData(requestUrl, expectHttpStatusCode, param);
        }
    }
}
