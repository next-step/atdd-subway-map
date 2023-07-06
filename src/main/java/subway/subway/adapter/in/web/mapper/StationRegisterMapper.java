package subway.subway.adapter.in.web.mapper;

import org.springframework.stereotype.Component;
import subway.subway.adapter.in.web.StationRegisterController;
import subway.subway.application.in.StationRegisterUsecase;

@Component
public class StationRegisterMapper {

    public StationRegisterUsecase.Command mapFrom(StationRegisterController.Request request) {
        return new StationRegisterUsecase.Command(request.getName());
    }
}
