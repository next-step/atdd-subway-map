package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.request.LineRequest;
import nextstep.subway.applicaion.dto.response.LineResponse;
import nextstep.subway.applicaion.dto.response.LineSaveResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicateRegistrationRequestException;
import nextstep.subway.exception.NotFoundRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    public static final String LINE_DUPLICATE_REGISTRATION_EXCEPTION_MESSAGE = "이미 등록된 노선입니다. 노선 이름 = %s";
    public static final String LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE = "존재하지 않는 노선입니다. id = %s";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineSaveResponse saveLine(LineRequest request) throws DuplicateRegistrationRequestException, NotFoundRequestException {
        // 전제 조건 : 역이 먼저 생성되어 있어야 함.
        Line findLine = lineRepository.findByName(request.getName());
        if (ObjectUtils.isEmpty(findLine)) {
            // 1. 각 역을 select한다.
            Station upStation = stationRepository.findById(request.getUpStationId())
                    .orElseThrow(() -> new NotFoundRequestException(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, request.getUpStationId())));
            Station downStation = stationRepository.findById(request.getDownStationId())
                    .orElseThrow(() -> new NotFoundRequestException(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, request.getDownStationId())));

            // 2. 라인의 객체를 생성한다.
            Line line = new Line(request.getName(), request.getColor());

            // 3. Section 객체에 셋팅한다.
            Section section = new Section(line, upStation, downStation, request.getDistance());
            line.addSection(section);

            // 4. line을 save한다.
            lineRepository.save(line);

            return LineSaveResponse.createLineResponse(line);
        }

        throw new DuplicateRegistrationRequestException(String.format(LINE_DUPLICATE_REGISTRATION_EXCEPTION_MESSAGE, request.getName()));
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) throws NotFoundRequestException {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequestException(String.format(LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, id)));

        return LineResponse.createLineResponse(line);
    }

    public void updateLineById(Long id, LineRequest lineRequest) throws NotFoundRequestException {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequestException(String.format(LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, id)));

        line.update(lineRequest.getName(), lineRequest.getColor());
        LineResponse.createLineResponse(line);
    }

    public void deleteLineById(Long id) throws NotFoundRequestException {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequestException(String.format(LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, id)));

        lineRepository.delete(line);
    }
}
