package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.SubwayLineDetailQuery;
import subway.subway.application.in.command.SubwayLineDetailQueryCommand;
import subway.subway.application.out.SubwayLineDetailQueryPort;
import subway.subway.application.query.SubwayLineResponse;

@Service
@Transactional(readOnly = true)
class SubwayLineDetailQueryService implements SubwayLineDetailQuery {

    private final SubwayLineDetailQueryPort subwayLineDetailQueryPort;

    public SubwayLineDetailQueryService(SubwayLineDetailQueryPort subwayLineDetailQueryPort) {
        this.subwayLineDetailQueryPort = subwayLineDetailQueryPort;
    }

    @Override
    public SubwayLineResponse findOne(SubwayLineDetailQueryCommand command) {
        return subwayLineDetailQueryPort.findOne(command.getId());
    }
}
