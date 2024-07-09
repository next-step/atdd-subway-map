package subway;

public final class SubwayLineRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;


    // 모든 필드를 초기화하는 생성자
    public SubwayLineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    // SubwayLine 객체로 변환하는 메서드
    public SubwayLine toSubwayLine() {
        return new SubwayLine(name, color, upStationId, downStationId, distance);
    }
}
