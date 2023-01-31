package subway.common;

public class Endpoints {
    public static final String STATIONS = "/stations";
    public static final String LINES = "/lines";

    public static final String sections(Long lineId) {
        return LINES + "/" + lineId + "/sections";
    }

    public static String endpointWithParam(String endpoint, Long id) {
        return String.format(endpoint + "/%s", id);
    }
}
