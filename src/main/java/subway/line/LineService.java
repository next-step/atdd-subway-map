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

import static subway.line.LineResponse.createLineResponse;

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
        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()));

        Station upStation = stationService.findById(lineRequest.getUpStationId());
        Station downStation = stationService.findById(lineRequest.getDownStationId());
        int distance = lineRequest.getDistance().intValue();

        line.addSection(upStation, downStation, distance);

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) throws CustomException {
        Line line = findLineById(id);
        line.setColor(lineRequest.getColor());
        lineRepository.save(line);
        Line updateLine = findLineById(id);
        return createLineResponse(updateLine);
    }


    public Line findLineById(Long id) throws CustomException {
        return lineRepository.findById(id).orElseThrow(()->new CustomException(
                new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ id +") 가 존재 하지 않습니다.")));
    }

    public LineResponse findLineResponseById(Long id) throws CustomException {
        createLineResponse(findLineById(id));
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

    @Transactional
    public Boolean addSectionByLineId(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        int distance = sectionRequest.getDistance();
        Line line = findLineById(lineId);

        // 추가하는 구간의 상행역이 기존 구간의 하행역이 맞는지 체크
        Boolean isLast = line.isLastSection(upStation.getId());
        if(!isLast){
            return false;
        }

        Boolean isExist = line.isStationExist(downStation);
        if(isExist){
            return false;
        }

        line.addSection(upStation, downStation, distance);

        return true;
    }

    @Transactional
    public Boolean deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(()->new CustomException(
                new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ lineId +") 가 존재 하지 않습니다.")));
        Station station = stationService.findById(stationId);

        // station이 존재 하지 않으면 예외 발생
        line.checkStationExist(station);

        // 지하철 노선에 등록된 역만 제거 가능. 즉 마지막 구간만 제거 가능
        Boolean isLast = line.isLastSection(stationId);
        if(!isLast){
            return false;
        }

        // 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제 할 수 없음
        Boolean isOnly = line.isOnlySection();
        if(isOnly){
            return false;
        }

        line.getSections().remove(line.getSections().size() - 1);

        return true;
    }
}
