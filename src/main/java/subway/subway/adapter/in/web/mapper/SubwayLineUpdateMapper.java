package subway.subway.adapter.in.web.mapper;

import org.springframework.stereotype.Component;
import subway.subway.adapter.in.web.SubwayLineUpdateController;
import subway.subway.application.in.command.SubwayLineUpdateCommand;
import subway.subway.domain.SubwayLine;

@Component
public class SubwayLineUpdateMapper {

    public SubwayLineUpdateCommand mapFrom(Long id, SubwayLineUpdateController.Request request) {
        SubwayLine.Id domainId = new SubwayLine.Id(id);
        SubwayLineUpdateCommand.UpdateContents contents = new SubwayLineUpdateCommand.UpdateContents(request.getName(), request.getColor());
        return new SubwayLineUpdateCommand(domainId, contents);
    }
}
