package subway.subway.adapter.in.web.mapper;

import org.springframework.stereotype.Component;
import subway.subway.adapter.in.web.SubwayLineUpdateController;
import subway.subway.application.in.SubwayLineUpdateUsecase;
import subway.subway.domain.SubwayLine;

@Component
public class SubwayLineUpdateMapper {

    public SubwayLineUpdateUsecase.Command mapFrom(Long id, SubwayLineUpdateController.Request request) {
        SubwayLine.Id domainId = new SubwayLine.Id(id);
        SubwayLineUpdateUsecase.Command.UpdateContents contents = new SubwayLineUpdateUsecase.Command.UpdateContents(request.getName(), request.getColor());
        return new SubwayLineUpdateUsecase.Command(domainId, contents);
    }
}
