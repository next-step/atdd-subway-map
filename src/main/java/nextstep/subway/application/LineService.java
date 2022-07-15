package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.common.exception.ResponseCode;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.ui.dto.line.CreateLineRequest;
import nextstep.subway.ui.dto.line.LineResponse;
import nextstep.subway.ui.dto.line.UpdateLineRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineResponse saveLine(CreateLineRequest request) {
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Line line = new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
        lineRepository.save(line);

        sectionRepository.save(Section.builder()
                                   .upStation(upStation)
                                   .downStation(downStation)
                                   .distance(request.getDistance())
                                   .line(line).build());

        return LineResponse.of(line, List.of(upStation, downStation));
    }

    public List<LineResponse> findAllLine() {
        return lineRepository.findAll().stream()
                             .map(line -> LineResponse.of(line, List.of(line.getUpStation(), line.getDownStation())))
                             .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long lineId) {
        Line line = findLine(lineId);
        List<Station> stations = List.of(line.getUpStation(), line.getDownStation());
        return LineResponse.of(line, stations);
    }

    public void updateLine(Long lineId, UpdateLineRequest request) {
        Line line = findLine(lineId);
        line.update(request.getName(), request.getColor());
    }

    public void deleteLine(final Long lineId) {
        lineRepository.deleteById(lineId);
    }

    private Station findStation(long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new CustomException(ResponseCode.STATION_NOT_FOUND));
    }

    private Line findLine(final Long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(() -> new CustomException(ResponseCode.LINE_NOT_FOUND));
    }
}
