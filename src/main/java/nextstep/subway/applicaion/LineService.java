package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());

        Section section = Section.create(upStation, downStation, lineRequest.getDistance());
        Line line = Line.create(lineRequest.getName(), lineRequest.getColor(), section);
        lineRepository.save(line);

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLineResponse() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
