package subway.domain;

public class LineCreateDomain {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public LineCreateDomain(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

}
