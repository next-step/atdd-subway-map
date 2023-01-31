package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.DomainException;
import subway.common.DomainExceptionType;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = request.toEntity();

        line = lineRepository.save(line);

        Optional<Station> downStation = stationRepository.findById(request.getDownStationId());
        Optional<Station> upStation = stationRepository.findById(request.getUpStationId());

        if(downStation.isPresent()){
            line.addStation(downStation.get());
        }else{
            line.setDownStationId(0L);
        }

        if(upStation.isPresent()){
            line.addStation(upStation.get());
        }else{
            line.setUpStationId(0L);
        }



        return LineResponse.entityToResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::entityToResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id).map(LineResponse::entityToResponse).orElse(null);
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line =
                lineRepository
                        .findById(id)
                        .orElseThrow(() -> new DomainException(DomainExceptionType.NO_LINE));

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
