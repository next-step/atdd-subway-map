package subway.controller.request;


import subway.service.command.SubwayLineCreateCommand;

public class SubwayLineCreateRequest implements SubwayLineCreateCommand {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;


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

    public Long getDistance() {
        return distance;
    }
}
