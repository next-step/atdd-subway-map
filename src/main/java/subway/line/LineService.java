package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) throws Exception {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(
                () -> new Exception("check up-station")
        );

        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(
                () -> new Exception("check down-station")
        );

        Line line = Line.builder().name(lineRequest.getName())
                .color(lineRequest.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineRequest.getDistance())
                .build();

        lineRepository.save(line);
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.toResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(LineResponse::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineById(Long id) {
        try {
            lineRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);

        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());

        lineRepository.save(line);
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return LineResponse.toResponse(line);
    }
}
