package subway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.model.request.LineRequest;
import subway.model.response.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName()
                                                , lineRequest.getColor()
                                                , lineRequest.getUpStationId()
                                                , lineRequest.getDownStationId()
                                                , lineRequest.getDistance()));
        return createLineResponse(line);
    }

    @Transactional
    public List<LineResponse> findAllLine(){
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse findLine(Long id){
        return createLineResponse(lineRepository.findById(id).get());
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());

        return createLineResponse(lineRepository.save(line));
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = lineRepository.findById(id).get();

        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stationRepository.findById(line.getUpStationId()).get(),
                stationRepository.findById(line.getDownStationId()).get(),
                line.getDistance()
        );
    }

}
