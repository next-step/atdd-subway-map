package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository,
        StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = new Line(lineRequest, lineRequest.getUpStationId(), lineRequest.getDownStationId());
        Line savedLine = lineRepository.save(line);

        Station upStation = stationRepository.findById(savedLine.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(savedLine.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        return LineResponse.from(savedLine, upStation, downStation);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(line -> {
                Station upStation = stationRepository.findById(line.getUpStationId())
                    .orElseThrow(IllegalArgumentException::new);
                Station downStation = stationRepository.findById(line.getDownStationId())
                    .orElseThrow(IllegalArgumentException::new);

                return LineResponse.from(line, upStation, downStation);
            }).collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        return LineResponse.from(line, upStation, downStation);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.updateLine(request);

        Line updateLine = lineRepository.save(line);
        Station upStation = stationRepository.findById(updateLine.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(updateLine.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        return LineResponse.from(updateLine, upStation, downStation);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

}
