package subway.subway.adapter.in.web.mapper;

import org.springframework.stereotype.Component;
import subway.subway.application.in.SubwayLineDetailQuery;
import subway.subway.domain.SubwayLine;

@Component
public class SubwayLineDetailQueryMapper {

    public SubwayLineDetailQuery.Command mapFrom(Long id) {
        SubwayLine.Id domainId = new SubwayLine.Id(id);
        return new SubwayLineDetailQuery.Command(domainId);
    }
}
