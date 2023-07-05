package subway.subway.adapter.in.web;

import org.springframework.stereotype.Component;
import subway.subway.application.in.command.StationRegisterCommand;

@Component
public class StationRegisterMapper {

    public StationRegisterCommand mapFrom(StationRegisterController.Request request) {
        return new StationRegisterCommand(request.getName());
    }
}
