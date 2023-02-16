package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CustomException;
import subway.exception.ErrorDto;
import subway.section.SectionRequest;
import subway.station.Station;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

//        Line line = lineRepository.save(new Line(
//                lineRequest.getName(),
//                lineRequest.getColor(),
//                lineRequest.getUpStationId(),
//                lineRequest.getDownStationId(),
//                lineRequest.getDistance()));

        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor()));
        Station upStation = stationService.findById(lineRequest.getUpStationId());
        Station downStation = stationService.findById(lineRequest.getDownStationId());
        line.getSections().add(new Section(line, upStation, downStation, lineRequest.getDistance().intValue()));

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) throws CustomException {
        Line line = findLineById(id);
        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());
        line.setUpStationId(lineRequest.getUpStationId());
        line.setDownStationId(lineRequest.getDownStationId());
        line.setDistance(lineRequest.getDistance());
        lineRepository.save(line);
        return createLineResponse(line);
    }


    public Line findLineById(Long id) throws CustomException {
        return lineRepository.findById(id).orElseThrow(()->new CustomException(
                new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ id +") 가 존재 하지 않습니다.")));
    }

    public LineResponse findLineResponseById(Long id) throws CustomException {
        return createLineResponse(findLineById(id));
    }


    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void deleteLines() {
        lineRepository.deleteAll();
    }

    private LineResponse createLineResponse(Line line) {
        List<Section> sections = line.getSections();
        List<StationResponse> stations = new ArrayList<>();

        for(int i=0; i<sections.size(); i++){
            if(i==0){
                Station upStation = sections.get(i).getUpStation();
                StationResponse stationResponse = new StationResponse(upStation.getId(), upStation.getName());
                stations.add(stationResponse);
            }

            Station downStation = sections.get(i).getDownStation();
            StationResponse stationResponse = new StationResponse(downStation.getId(), downStation.getName());
            stations.add(stationResponse);
        }

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations
        );
    }

    @Transactional
    public void addSectionByLineId(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = findLineById(lineId);
        line.getSections().add(new Section(
                line
                , upStation
                , downStation
                , sectionRequest.getDistance()));
    }
}
