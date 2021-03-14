package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return persistLine.getLineResponse();
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllStations() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> line.getLineResponse())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findStationById(Long id) {
        Line line = lineRepository.getOne(id);
        return line.getLineResponse();
    }

    public void modifyLine(LineRequest request, Long id) {
        Line line = lineRepository.getOne(id);
        line.update(request.getName(), request.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    /**
     * 지하철 노선에 구간을 등록
     */
    public LineResponse registerLineSection(Long lineId, LineRequest request, Station upStation, Station downStation) {
        Line line = lineRepository.getOne(lineId);

        // 노선 업데이트
        line.update(line.getName(), line.getColor(), upStation, downStation, request.getDistance());

        return line.getLineResponse();
    }

    /**
     * 지하철 노선에 구간을 제거
     */
    public LineResponse deleteLineSection(Long lineId, Long stationId) {
        Line line = lineRepository.getOne(lineId);

        // 마지막 노선 제거
        line.getSections().removeLastSection(stationId);

        return line.getLineResponse();
    }

}
