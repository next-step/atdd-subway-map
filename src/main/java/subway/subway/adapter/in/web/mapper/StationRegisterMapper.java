package subway.subway.adapter.in.web.mapper;

import org.springframework.stereotype.Component;
import subway.subway.adapter.in.web.StationRegisterController;
import subway.subway.application.in.command.StationRegisterCommand;

@Component
public class StationRegisterMapper {

    public StationRegisterCommand mapFrom(StationRegisterController.Request request) {
        return new StationRegisterCommand(request.getName());
    }
}
