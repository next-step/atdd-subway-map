package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.SubwayLineCloseUsecase;
import subway.subway.application.out.SubwayLineClosePort;

@Service
@Transactional
class SubwayLineCloseService implements SubwayLineCloseUsecase {

    private final SubwayLineClosePort subwayLineClosePort;

    SubwayLineCloseService(SubwayLineClosePort subwayLineClosePort) {
        this.subwayLineClosePort = subwayLineClosePort;
    }

    @Override
    public void closeSubwayLine(SubwayLineCloseUsecase.Command command) {
        subwayLineClosePort.closeSubwayLine(command.getId());
    }
}
