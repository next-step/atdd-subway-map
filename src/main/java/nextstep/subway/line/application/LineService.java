package nextstep.subway.line.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.exception.SubwayNameDuplicateException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    private StationRepository stationRepository;

    public LineService(StationRepository stationRepository,
                       LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());

        boolean isExistSubwayName = lineRepository.existsByName(request.getName());
        if (isExistSubwayName) {
            throw new SubwayNameDuplicateException();
        }
        Line line = Line.of(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> getAll() {
        List<Line> lines = lineRepository.findAll();
        if (lines.isEmpty()) {
            return Collections.emptyList();
        }

        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    private Line getLine(long id) {
        return Optional.of(lineRepository.findById(id))
                .orElseThrow(NullPointerException::new)
                .get();
    }

    public LineResponse get(long id) {
        return LineResponse.of(getLine(id));
    }

    public LineResponse saveLine(Long id, LineRequest lineRequest) {
        Line line = getLine(id);
        line.update(lineRequest.toLine());

        lineRepository.save(line);
        return LineResponse.of(line);
    }

    public void delete(long id) {
        Line line = getLine(id);
        lineRepository.delete(line);
    }

    public LineResponse addSection(long lineId, SectionRequest sectionRequest) {
        Line line = findLineById(lineId);

        Station upStation = findStationById(sectionRequest.getUpStationId());
        Station downStation = findStationById(sectionRequest.getDownStationId());

        line.addSection(upStation, downStation, sectionRequest.getDistance());
        return LineResponse.of(line);
    }

    public LineResponse deleteSection(long lineId, long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);

        line.deleteSection(station.getId());
        return LineResponse.of(line);
    }

    private Line findLineById(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException("지하철 노선"));
    }

    private Station findStationById(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("지하철역"));
    }
}
