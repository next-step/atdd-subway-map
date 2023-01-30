package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).get();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).get();

        Line line = lineRepository.save(new Line(
                null,
                lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation,
                lineRequest.getDistance()));

        return createLineResponse(line);
    }

    public List<LineResponse> readLines() {
        return lineRepository
                .findAll()
                .stream()
                .map(line -> createLineResponse(line))
                .collect(Collectors.toList());
    }

    public LineResponse readLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                        createStationResponse(line.getUpStation()),
                        createStationResponse(line.getDownStation())
                )
        );
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
