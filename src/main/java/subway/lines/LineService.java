package subway.lines;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLines(LineCreateRequest lineCreateRequest) {
        final Station upStation = stationRepository
            .findById(lineCreateRequest.getUpStationId())
            .orElse(null);
        final Station downStation = stationRepository
            .findById(lineCreateRequest.getDownStationId())
            .orElse(null);

        final Line line = lineRepository.save(
            new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                upStation,
                downStation,
                lineCreateRequest.getDistance()
            )
        );

        return new LineResponse(line);
    }

    public List<LineResponse> getLinesList() {
        return lineRepository.findAll().stream().map(LineResponse::new)
            .collect(Collectors.toList());
    }

    public LineResponse getLines(Long id) {
        final Line line = lineRepository.findById(id).orElse(null);
        if (line == null) {
            return null;
        }

        return new LineResponse(line);
    }

    @Transactional
    public void updateLines(Long id, LineUpdateRequest lineUpdateRequest) {
        final Line line = lineRepository.findById(id).orElse(null);
        if (line == null) {
            throw new EntityNotFoundException();
        }

        line.setName(lineUpdateRequest.getName());
        line.setColor(lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLines(Long id) {
        lineRepository.deleteById(id);
    }
}
