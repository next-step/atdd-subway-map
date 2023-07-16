package subway.subway.application.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.query.SubwayLineDetailQuery;
import subway.subway.application.out.query.SubwayLineDetailQueryPort;
import subway.subway.application.query.SubwayLineResponse;

@Service
@Transactional(readOnly = true)
class SubwayLineDetailQueryService implements SubwayLineDetailQuery {

    private final SubwayLineDetailQueryPort subwayLineDetailQueryPort;

    public SubwayLineDetailQueryService(SubwayLineDetailQueryPort subwayLineDetailQueryPort) {
        this.subwayLineDetailQueryPort = subwayLineDetailQueryPort;
    }

    @Override
    public SubwayLineResponse findOne(Command command) {
        return subwayLineDetailQueryPort.findOne(command.getId());
    }
}
