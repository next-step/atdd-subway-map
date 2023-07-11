package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.exception.NotFoundException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.SectionRequest;
import subway.line.dto.SectionResponse;

import java.util.List;
import java.util.stream.Collectors;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line line = lineRepository.save(lineRequest.toEntity());

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {

        return lineRepository.findAll()
            .stream().map(LineResponse::from)
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(long id) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));

        return LineResponse.from(line);
    }

    @Transactional
    public LineResponse updateLine(long id, LineRequest lineRequest) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        line.update(lineRequest);

        return LineResponse.from(line);
    }

    @Transactional
    public void deleteLine(long id) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        lineRepository.delete(line);
    }

    @Transactional
    public LineResponse addSection(long id, SectionRequest request) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        Station upStation = stationRepository.findById(request.getUpStationId())
            .orElseThrow(() -> new NotFoundException(request.getUpStationId()));
        Station downStation = stationRepository.findById(request.getDownStationId())
            .orElseThrow(() -> new NotFoundException(request.getDownStationId()));

        line.addSection(new Section(line, upStation, downStation, request.getDistance()));

        return LineResponse.from(line);
    }

    public void deleteLastSection(long id, long stationId) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException(stationId));

        line.deleteSection(station);
    }
}
