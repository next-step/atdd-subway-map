package nextstep.subway.applicaion.line;

import java.util.Objects;
import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.station.StationReadService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineCreationService {
    // 사용 도메인 서비스들
    private final LineModifyService lineModifyService;
    private final StationReadService stationReadService;

    public LineCreationService(
            LineModifyService lineModifyService,
            StationReadService stationReadService) {
        this.lineModifyService = lineModifyService;
        this.stationReadService = stationReadService;
    }

    @Transactional
    public LineCreateResponse saveLine(LineRequest lineRequest) {
        // TODO Validation 코드들 공통 부분은 클래스로 빼기
        // Not null Validation
        Objects.nonNull(lineRequest.getUpStationId());
        Objects.nonNull(lineRequest.getDownStationId());
        Objects.nonNull(lineRequest.getDistance());

        Line line = lineModifyService.saveLine(lineRequest.getName(), lineRequest.getColor());
        Station upStation = stationReadService.findSpecificStation(lineRequest.getUpStationId());
        Station downStation = stationReadService.findSpecificStation(lineRequest.getUpStationId());

        Section section = new Section(line, upStation, downStation, lineRequest.getDistance());
        line.addSection(section);
        return LineCreateResponse.of(line);
    }
}
