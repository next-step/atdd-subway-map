package subway.controller.request;

import subway.service.command.StationCreateCommand;

public class StationCreateRequest implements StationCreateCommand {
    private String name;

    public String getName() {
        return name;
    }
}
