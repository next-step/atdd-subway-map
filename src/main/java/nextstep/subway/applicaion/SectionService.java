package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.NotFoundRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(Long lineId, SectionRequest request) throws NotFoundRequestException {
        // 1. lineId로 해당 라인을 조회한다.
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundRequestException(String.format(LineService.LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, lineId)));
//
//        // 2. 상행역을 조회한다.
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new NotFoundRequestException(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, request.getUpStationId())));
//
//        // 3. 하행역을 조회한다.
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new NotFoundRequestException(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, request.getDownStationId())));
//
//        // 4. 구간을 생성한다.
        Section section = new Section(line, upStation, downStation, request.getDistance());
        line.addSection(section);
//
//        // 5. 구간을 save한다.
        sectionRepository.save(section);
        return SectionResponse.createSectionResponse(section, lineId);
    }
}
