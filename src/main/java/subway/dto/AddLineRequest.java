package subway.dto;

public class AddLineRequest {

    private Long downStationId;
    private Long upStationId;
    private int distance;

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }

    public void compareUpStationId(Long prevDownStationId) {
        if(!this.upStationId.equals(prevDownStationId)){
            throw new RuntimeException("새로운 지하철의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }
    }
}
