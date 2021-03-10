package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.exception.NoStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(NoStationException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(NoStationException::new);

        Section section = Section.of(upStation, downStation, request.getDistance());
        Line line = request.toLine();
        line.addSection(section);

        lineRepository.save(line);

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(final Long id) {
        return lineRepository.findById(id)
                .map(l -> LineResponse.of(l))
                .orElseThrow(() -> new LineNotFoundException());
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException());
        line.update(lineRequest.toLine());
    }

    @Transactional
    public void deleteLine(final Long id) {
        lineRepository.deleteById(id);
    }
}
