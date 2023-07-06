package subway;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private final SubwayLineRepository lineRepository;
    private final StationRepository stationRepository;

    public SubwayLineService(SubwayLineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SubwayLineResponse saveLine(CreateSubwayLineRequest request) {
        final Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found station"));
        final Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found station"));

        SubwayLine subwayLine = SubwayLine.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
        return SubwayLineResponse.toResponse(lineRepository.save(subwayLine));
    }

    @Transactional
    public void updateLine(Long lineId, UpdateSubwayLineRequest request) {
        final SubwayLine line = lineRepository.findById(lineId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "wrong subwayLine id"));
        line.change(request);
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public List<SubwayLineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(SubwayLineResponse::toResponse)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findByLineId(Long id) {
        return SubwayLineResponse.toResponse(lineRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found subway line")));
    }
}
