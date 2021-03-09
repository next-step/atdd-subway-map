package nextstep.subway.line.application;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.InvalidSectionException;
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

        if(line.getSections().size() == 0) {
            line.addSection(new Section(line,upStation,downStation,sectionRequest.getDistance()));
            return;
        }

        //하행종점은 다른 구간의 상행역이 아니다
        //구간의 상행역으로 등록되지 않은 하행역이 하행종점
        boolean isValidUpStation =  !line.getSections().get(line.getSections().size()-1)
            .getDownStation().equals(upStation);

        boolean isValidDownStation = line.getSections().stream()
            .anyMatch(section -> section.getUpStation().equals(downStation) || section.getDownStation().equals(downStation));

        if(isValidUpStation) throw new InvalidSectionException("상행역은 현재 노선의 하행 종점역이어야 합니다.");
        if(isValidDownStation) throw new InvalidSectionException("하행역은 노선에 이미 등록되어 있습니다.");

        line.addSection(new Section(line,upStation,downStation,sectionRequest.getDistance()));



    }
}
