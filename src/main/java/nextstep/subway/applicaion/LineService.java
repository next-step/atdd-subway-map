package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;
    public LineService(LineRepository lineRepository, StationService stationService){
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }
    @Transactional
    public LineResponse saveLine(LineRequest lineRequest){
        Line line = lineRepository.save(new Line(lineRequest));
        return createLineResponse(line);
    }
    @Transactional
    public void updateLine(Long id, LineRequest lineRequest){
        Line line = lineRepository.findById(id).get();
        lineRepository.updateNameAndColor(line.getId(), lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id){
        lineRepository.deleteById(id);
    }



    public List<LineResponse> findAllLines(){
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id){
        Optional<Line> line = lineRepository.findById(id);
        if(line.isPresent()){
            this.createLineResponse(line.get());
        }
        return null;
    }

    private LineResponse createLineResponse(Line line){
        LineResponse response = new LineResponse(line);
        response.setStation(stationService.findStationById(line.getUpStationId()));
        response.setStation(stationService.findStationById(line.getDownStationId()));
        return response;
    }

}
