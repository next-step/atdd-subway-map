package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLine(final LineRequest lineRequest) {

        final Station upStation = stationService.findById(lineRequest.getUpStationId());
        final Station downStation = stationService.findById(lineRequest.getDownStationId());
        final Line saveLine = lineRepository.save(lineRequest.toEntity());
        saveLine.addLine(upStation);
        saveLine.addLine(downStation);

        return LineResponse.createResponse(saveLine);
    }

    public List<LineResponse> showLines() {

        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::createResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(final long lineId) {

        final Line findLine = findById(lineId);

        return LineResponse.createResponse(findLine);
    }

    @Transactional
    public void updateLine(final long lineId, final LineRequest lineRequest) {

        final Line findLine = findById(lineId);

        findLine.updateLine(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(final long lineId) {

        lineRepository.deleteById(lineId);
    }

    private Line findById(long lineId) {

        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 호선입니다."));
    }
}
