package nextstep.subway.applicaion.service;

import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.LineCreationResponse;
import nextstep.subway.applicaion.dto.request.LineRequest;
import nextstep.subway.applicaion.dto.response.LineResponse;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import nextstep.subway.domain.Entity.Line;
import nextstep.subway.domain.Entity.Section;
import nextstep.subway.domain.Entity.Station;
import nextstep.subway.domain.Repository.LineRepository;
import nextstep.subway.domain.Repository.SectionRepository;
import nextstep.subway.domain.Repository.StationRepository;
import nextstep.subway.exception.DuplicationException;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(SectionRepository sectionRepository,
                       LineRepository lineRepository,
                       StationRepository stationRepository
    ) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineCreationResponse saveLine(LineRequest request) {
        verifyDuplication(request.getName());
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(()-> new NotFoundException("존재하지 않는 역입니다. 역 id : " + request.getUpStationId()));
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(()-> new NotFoundException("존재하지 않는 역입니다. 역 id : " + request.getDownStationId()));

        Line line = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance()));

        sectionRepository.save(new Section(line, upStation, downStation, request.getDistance()));

        return new LineCreationResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate(),
                line.getSections()
        );
    }

    private void verifyDuplication(final String name) {
        Line line = lineRepository.findByName(name);

        if (!ObjectUtils.isEmpty(line)) {
            throw new DuplicationException("이미 존재합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);

        return createLineResponse(line);
    }

    public void updateLine(final Long id, final LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        line.updateLine(request.getColor(), request.getName());
        lineRepository.save(line);
    }

    public void deleteLine(final Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        lineRepository.delete(line);
    }

    private LineResponse createLineResponse(final Line line) {

        List<Station> stations = new ArrayList<>();

        for (Section section : line.getSections()) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        List<Station> newStations = stations.stream().distinct().collect(Collectors.toList());

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                newStations,
                line.getCreatedDate(),
                line.getModifiedDate());
    }

    public SectionResponse createSection(final Long id, final SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("노선이 존재하지 않습니다." + "LineId : " + id));
        Station upStation =
                stationRepository.findById(sectionRequest.getUpStationId())
                        .orElseThrow(() -> new NotFoundException("상행역이 존재하지 않습니다." + "LineId : " + id));
        Station downStation =
                stationRepository.findById(sectionRequest.getDownStationId())
                        .orElseThrow(() -> new NotFoundException("하행역이 존재하지 않습니다." + "LineId : " + id));
        if (line.isExistSection(downStation)) {
            throw new DuplicationException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.");
        }
        if (!line.isDownStation(upStation.getId())) {
            throw new RuntimeException("등록하고자 하는 상행역은 하행역(종점)이여야한다.");
        }
        Section section = sectionRepository.save(Section.of(line, upStation, downStation, sectionRequest.getDistance()));
        line.updateDownStation(downStation);

        return SectionResponse.of(section.getId(), upStation.getId(), downStation.getId(), section.getDistance());
    }

    public void deleteSection(final Long lineId, final Long stationId) {

        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException("노선이 존재하지 않습니다." + "LineId : " + lineId));
        if (!line.isDownStation(stationId)) {
            throw new RuntimeException("삭제하고자 하는 역이 종점역이 아닙니다. id : " + stationId);
        }
        Station downStation = stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다." + "stationId : " + stationId));

        Section section = sectionRepository.findByDownStation(downStation)
                .orElseThrow(() -> new NotFoundException("구간이 존재하지 않습니다."));
        if (1 == line.sectionCount()) {
            throw new RuntimeException("구간이 1개 있는 경우 역을 삭제 할 수 없다.");
        }

        sectionRepository.delete(section);
    }
}
