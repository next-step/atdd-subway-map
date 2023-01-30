package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.DomainException;
import subway.common.DomainExceptionType;
import subway.station.StationResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(CreateLineRequest request){
        Line line = lineRepository.save(request.toEntity());

        return LineResponse.entityToResponse(line);
    }

    public List<LineResponse> findAllLines(){
        return lineRepository.findAll().stream().map(LineResponse::entityToResponse).collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id){
        return lineRepository.findById(id).map(LineResponse::entityToResponse).orElse(null);
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request){
        Line line = lineRepository.findById(id).orElseThrow(
                () -> new DomainException(DomainExceptionType.NO_LINE)
        );

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id){
        lineRepository.deleteById(id);
    }
}
