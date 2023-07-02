package subway.controller.request;

import subway.service.command.StationLineModifyCommand;

public class StationLineModifyRequest implements StationLineModifyCommand {

    private String name;
    private String color;

    public StationLineModifyRequest() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
