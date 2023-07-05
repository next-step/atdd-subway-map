package subway.subway.adapter.in.web.mapper;

import org.springframework.stereotype.Component;
import subway.subway.application.in.command.SubwayLineDetailQueryCommand;
import subway.subway.domain.SubwayLine;

@Component
public class SubwayLineDetailQueryMapper {

    public SubwayLineDetailQueryCommand mapFrom(Long id) {
        SubwayLine.Id domainId = new SubwayLine.Id(id);
        return new SubwayLineDetailQueryCommand(domainId);
    }
}
