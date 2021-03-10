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
    private SectionService sectionService;

    public LineService(LineRepository lineRepository, StationService stationService, SectionService sectionService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public LineResponse creteLine(LineRequest request) {

        validateLineRequest(request);

        Line line = request.toLine();

        Line persistLine;
        try {
            persistLine = lineRepository.save(request.toLine());
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

    //https://edu.nextstep.camp/s/b7KHeSY2/ls/AgSPspmB
    //노선 생성 시 종점역 추가
    //구간 등록
    /*
    지하철 노선에 구간을 등록하는 기능을 구현
    새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.
    새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.
    새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */

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

    public Line createLineSection(Long id, SectionRequest sectionRequest) {
        Optional<Line> line = lineRepository.findOneById(id);
        List<Section> sections = sectionService.getSectionsByLineId(id);

        line.orElseThrow(() -> new ApplicationException(ApplicationType.INVALID_ID));

        if (line.get().validateExtentionAvailable(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sections)) {
            throw new ApplicationException(ApplicationType.INVALID_STATION_ID);
        }

        Section section = sectionService.createSection(id, sectionRequest);
        line.get().extendsLine(section.getDownStationId(), section.getDistance());

        return line.get();
    }
}
