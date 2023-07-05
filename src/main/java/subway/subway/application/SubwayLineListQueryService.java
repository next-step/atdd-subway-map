package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.SubwayLineListQuery;
import subway.subway.application.out.SubwayLineListQueryPort;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

import java.util.List;

@Service
@Transactional
class SubwayLineListQueryService implements SubwayLineListQuery {

    private final SubwayLineListQueryPort subwayLineListQueryPort;

    public SubwayLineListQueryService(SubwayLineListQueryPort subwayLineListQueryPort) {
        this.subwayLineListQueryPort = subwayLineListQueryPort;
    }

    @Override
    public List<SubwayLineResponse> findAll() {
        return subwayLineListQueryPort.findAll();
    }
}
