package nextstep.subway.applicaion.dto.line;

public class SectionRequest {

    private String downStationId;

    private String upStationId;

    private int distance;

    public SectionRequest(String downStationId, String upStationId, int distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }
}
