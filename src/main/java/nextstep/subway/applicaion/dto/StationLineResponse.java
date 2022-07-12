package nextstep.subway.applicaion.dto;


public class StationLineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;

    public StationLineResponse(Long id, String name, String color, Long upStationId, Long downStationId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

}
