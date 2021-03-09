package nextstep.subway.line.application;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.common.exception.NoResourceException;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {

        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getLines() {
        List<Line> lineList =  lineRepository.findAll();
        return lineList.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(long id) {
        Line line = lineRepository.findById(id).orElseThrow(()-> new NoResourceException("노선을 찾을수 없습니다."));
        return LineResponse.of(line);

    }

    public LineResponse modifyLine(long id,LineRequest lineRequest) {
        Line line  =  lineRepository.findById(id).orElseThrow(()-> new NoResourceException("노선을 찾을수 없습니다."));
        line.update(lineRequest.toLine());
        return LineResponse.of(line);
    }

    public void removeLine(long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(long lineId, SectionRequest sectionRequest) {

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
            .orElseThrow(()-> new NoResourceException("상행역을 찾을수 없습니다."));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
            .orElseThrow(()-> new NoResourceException("하행역을 찾을수 없습니다."));
        Line line = lineRepository.findById(lineId)
            .orElseThrow(()-> new NoResourceException("노선을 찾을수 없습니다."));

        line.addSection(new Section(line,upStation,downStation,sectionRequest.getDistance()));

    }
}
