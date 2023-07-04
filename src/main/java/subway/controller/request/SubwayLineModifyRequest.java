package subway.controller.request;

import subway.service.command.SubwayLineModifyCommand;

public class SubwayLineModifyRequest implements SubwayLineModifyCommand {

    private String name;
    private String color;

    public SubwayLineModifyRequest() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
