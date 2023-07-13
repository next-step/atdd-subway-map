package subway.subway.application.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.query.SubwayLineListQuery;
import subway.subway.application.out.query.SubwayLineListQueryPort;
import subway.subway.application.query.SubwayLineResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
