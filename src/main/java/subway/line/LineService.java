package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public Line saveLine(CreateLineRequest request) {
        return lineRepository.save(
                Line.builder()
                        .name(request.getName())
                        .color(request.getColor())
                        .upStation(stationRepository.getReferenceById(request.getUpStationId()))
                        .downStation(stationRepository.getReferenceById(request.getDownStationId()))
                        .build()
        );
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
        line.setName(request.getName());
        line.setColor(request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
