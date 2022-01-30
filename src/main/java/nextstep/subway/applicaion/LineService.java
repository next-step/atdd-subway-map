package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.request.LineRequest;
import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.LineResponse;
import nextstep.subway.applicaion.dto.response.LineSaveResponse;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.BadRequestException;
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
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineSaveResponse saveLine(LineRequest request) throws DuplicateRegistrationRequestException, NotFoundRequestException {
        Line findLine = lineRepository.findByName(request.getName());
        if (ObjectUtils.isEmpty(findLine)) {
            Station upStation = findStation(request.getUpStationId());
            Station downStation = findStation(request.getDownStationId());
            Line line = Line.createLine(request.getName(), request.getColor());

            Section section = Section.createNewLineSection(line, upStation, downStation, request.getDistance());
            line.addSection(section);

            lineRepository.save(line);

            return LineSaveResponse.createLineResponse(line);
        }

        throw new DuplicateRegistrationRequestException(String.format(LINE_DUPLICATE_REGISTRATION_EXCEPTION_MESSAGE, request.getName()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> {
                    List<Section> sections = sectionRepository.findByLineOrderByIdAsc(line);
                    return LineResponse.createLineResponse1(line, sections);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) throws NotFoundRequestException {
        Line line = findLine(id);
        List<Section> sections = sectionRepository.findByLineOrderByIdAsc(line);
        return LineResponse.createLineResponse1(line, sections);
    }

    public void updateLineById(Long id, LineRequest lineRequest) throws NotFoundRequestException {
        Line line = findLine(id);
        line.update(lineRequest.getName(), lineRequest.getColor());

        LineResponse.createLineResponse(line);
    }

    public void deleteLineById(Long id) throws NotFoundRequestException {
        Line line = findLine(id);
        lineRepository.delete(line);
    }

    public SectionResponse saveSection(Long lineId, SectionRequest request) throws NotFoundRequestException, BadRequestException {
        Line line = findLine(lineId);
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Section section = Section.createAddSection(line, upStation, downStation, request.getDistance());
        line.addSection(section);

        return SectionResponse.createSectionResponse(section, lineId);
    }

    public void deleteStationById(Long lineId, Long stationId) {
        Line line = findLine(lineId);
        line.deleteSection(stationId);
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundRequestException(String.format(LineService.LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, lineId)));
    }

    private Station findStation(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new NotFoundRequestException(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, upStationId)));
    }
}
