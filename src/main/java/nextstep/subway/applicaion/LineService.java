package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Line saveLine = lineRepository.save(
                new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
                        lineRequest.getDownStationId(), lineRequest.getDistance())
        );
        return LineResponse.of(saveLine, findAllStationUsingLine(saveLine));
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
        return LineResponse.of(line, findAllStationUsingLine(line));
    }
    @Transactional(readOnly = true)
    public List<LineResponse> findAllLine(){
        return lineRepository.findAll()
                .stream()
                .map(line -> LineResponse.of(line, findAllStationUsingLine(line)))
                .collect(Collectors.toList())
                ;
    }

    private List<StationResponse> findAllStationUsingLine(Line line){
        return stationRepository.findAllById(
                List.of(line.getUpStationId(), line.getDownStationId()))
                .stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList())
                ;
    }

    public void modifyLine(Long lineId, LineRequest lineRequest){
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
        line.modify(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

}
