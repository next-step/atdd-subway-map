package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {

        Station upStation = findStationById(lineRequest.getUpStationId());
        Station downStation = findStationById(lineRequest.getDownStationId());

        Line saveLine = lineRepository.save(
                new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance()
                , upStation, downStation)
        );
        return LineResponse.of(saveLine);
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(long id) {

        Line line =  findLineOne(id);

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLine(){
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }

    public void modifyLine(Long lineId, LineRequest lineRequest){
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));

        line.modifyLine(lineRequest.getName(), lineRequest.getColor());
    }


    public void deleteLineById(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException("삭제 대상을 찾을 수 없습니다."));
        lineRepository.deleteById(lineId);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() ->
                new IllegalArgumentException("해당역이 없습니다.")
        );
    }


    public LineResponse addSection(Long id, SectionRequest sectionRequest) {

        Line line = findLineOne(id);
        line.vlidationSectionStation(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());

        Station upStation = findStationById(sectionRequest.getUpStationId());
        Station downStation = findStationById(sectionRequest.getDownStationId());
        Section section = new Section(sectionRequest.getDistance(), upStation, downStation);
        line.addSection(section);
        line.modifyDownStationId(sectionRequest.getDownStationId());

        return LineResponse.of(line);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineOne(lineId);
        Section lastSection = line.validationAndSectionDelete(line, stationId);
        stationRepository.delete(lastSection.getDownStation());
        line.modifyDownStationId(lastSection.getUpStation().getId());
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findAllSection(){

        List<Line> lines =  lineRepository.findAll();
        List<Section> sections = new ArrayList<>();

        lines.forEach(
                list -> sections.addAll(list.getSectionList())
        );
        return sections.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
    }

    private Line findLineOne(Long id){
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
    }
}
