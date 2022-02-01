package nextstep.subway.applicaion.line;

import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.station.StationReadService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class LineCreationService {
    // 사용 도메인 서비스들
    private final LineModifyService lineModifyService;
    private final StationReadService stationReadService;

    public LineCreationService(
            LineModifyService lineModifyService, StationReadService stationReadService) {
        this.lineModifyService = lineModifyService;
        this.stationReadService = stationReadService;
    }

    // TODO transactional 없이 해당 메서드 수행 시 트랜잭션 2번 수행
    public LineCreateResponse saveLine(LineRequest lineRequest) {

        Line line = lineModifyService.saveLine(lineRequest.getName(), lineRequest.getColor());

        if (lineRequest.getUpStationId() != null
                && lineRequest.getDownStationId() != null
                && lineRequest.getDistance() != 0) {
            Station upStation =
                    stationReadService.findSpecificStation(lineRequest.getUpStationId());
            Station downStation =
                    stationReadService.findSpecificStation(lineRequest.getDownStationId());
            Section section = new Section(upStation, downStation, lineRequest.getDistance());
            lineModifyService.addSection(line.getId(), section);
        }

        return LineCreateResponse.of(line);
    }
}
