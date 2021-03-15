package nextstep.subway.line.application;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse creteLine(LineRequest request) {

        validateLineRequest(request);

        Line persistLine;

        Station upStation = stationService.getStation(request.getUpStationId());
        Station downStation = stationService.getStation(request.getDownStationId());

        Section section = new Section(upStation, downStation, request.getDistance());
        try {

            persistLine = lineRepository.save(request.toLine());
            section.assignLine(persistLine);
            persistLine.extendsLine(section);

        } catch (PersistenceException exception) {
            throw new ApplicationException(ApplicationType.KEY_DUPLICATED);
        }


        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(long id) {
        Optional<Line> line = lineRepository.findOneById(id);
        return line.map(LineResponse::of).orElseThrow(() -> new ApplicationException(ApplicationType.CONTENT_NOT_FOUND));
    }

    public void deleteLineById(Long id) {
        Optional<Line> line = lineRepository.findOneById(id);
        line.orElseThrow(() -> new ApplicationException(ApplicationType.CONTENT_NOT_FOUND));

        lineRepository.delete(line.get());
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Optional<Line> persistLine = lineRepository.findOneById(id);

        Line line = persistLine.orElseThrow(() -> new ApplicationException(ApplicationType.CONTENT_NOT_FOUND));
        line.update(lineRequest.toLine());
        return  LineResponse.of(line);
    }

    //구간 제거
    /*
    지하철 노선에 구간을 제거하는 기능 구현
    지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.
    지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
    새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
    */

    //등록된 구간 통해 역 목록 조회
    /*
    지하철 노선 조회 시 등록된 역 목록을 함께 응답
    노선에 등록된 구간을 순서대로 정렬하여 상행 종점부터 하행 종점까지 목록을 응답하기
    * */

    private void validateLineRequest(LineRequest lineRequest) {
        lineRequest.validate();
        stationService.validateStationId(lineRequest.getDownStationId());
    }

    public LineResponse createLineSection(Long id, SectionRequest sectionRequest) {
        Optional<Line> line = lineRepository.findOneById(id);

        line.orElseThrow(() -> new ApplicationException(ApplicationType.INVALID_ID));

        Station upStation = stationService.getStation(sectionRequest.getUpStationId());
        Station downStation = stationService.getStation(sectionRequest.getDownStationId());

        Section section = new Section(upStation, downStation, sectionRequest.getDistance());

        if (!line.get().isExtensionAvailable(section)) {
            throw new ApplicationException(ApplicationType.INVALID_STATION_ID);
        }

        section.assignLine(line.get());
        line.get().extendsLine(section);

        return LineResponse.of(line.get());
    }

    public void deleteSection(Long id, Long stationId) {
        Optional<Line> line = lineRepository.findOneById(id);

        line.orElseThrow(() -> new ApplicationException(ApplicationType.INVALID_ID));

        //line 마지막값 가져와서 stationId가 맞는지를 validation 해
        if (!line.get().isRemovableStation(stationId)) {
            throw new ApplicationException(ApplicationType.INVALID_REQUEST_PARAMETER);
        }
        //삭제해
        line.get().removeSection(stationId);
    }
}
