package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.code.LineValidateTypeCode;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Stations;
import subway.line.dto.request.LineRequest;
import subway.line.dto.request.SectionRequest;
import subway.line.dto.response.LineResponse;
import subway.line.dto.response.SectionResponse;
import subway.line.repository.LineRepository;
import subway.station.domain.Station;
import subway.station.service.StationService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private static final String NAME_ALREADY_EXISTS = "이미 존재하는 노선 이름입니다.";
    private static final String LINE_DOES_NOT_EXIST = "존재하지 않는 노선입니다.";
    private static final String STATION_NOT_MATCHED = "노선이 일치하지 않습니다.";

    private final StationService stationService;

    private final SectionService sectionService;

    private final LineRepository lineRepository;

    public LineService(LineRepository LineRepository, StationService stationService, SectionService sectionService) {
        this.lineRepository = LineRepository;
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public LineResponse saveLine(LineValidateTypeCode LineValidateTypeCode, LineRequest LineRequest) {
        validateLine(LineValidateTypeCode, LineRequest);
        Line line = LineRequest.toEntity();
        Stations stations = stationService.getStations(LineRequest.getUpStationId(), LineRequest.getDownStationId());
        line.saveStations(stations);
        line = lineRepository.save(line);
        createSections(line, stations);
        return LineResponse.of(line);
    }


    public LineResponse inquiryLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(() -> new IllegalArgumentException(LINE_DOES_NOT_EXIST));
    }

    public List<LineResponse> inquiryLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteLine(Long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(LINE_DOES_NOT_EXIST));
        lineRepository.deleteById(id);
    }

    private void validateLine(LineValidateTypeCode lineValidateTypeCode, LineRequest LineRequest) {
        if (LineValidateTypeCode.UPDATE == lineValidateTypeCode) {
            validateLineIdAndName(LineRequest);
            return;
        }
        validateLineName(LineRequest);
    }

    private void validateLineName(LineRequest LineRequest) {
        lineRepository.findByName(LineRequest.getName())
                .ifPresent(Line -> {
                    throw new IllegalArgumentException(NAME_ALREADY_EXISTS);
                });
    }

    private void validateLineIdAndName(LineRequest LineRequest) {
        lineRepository.findByName(LineRequest.getName())
                .ifPresent(Line -> {
                    if (!Line.getId().equals(LineRequest.getId())) {
                        throw new IllegalArgumentException(NAME_ALREADY_EXISTS);
                    }
                });
    }

    private void createSections(Line line, Stations stations) {
        sectionService.createSections(
                Arrays.asList(
                        new Section(line, stations.getUpStation()),
                        new Section(line, stations.getDownStation())
                )
        );
    }

    public SectionResponse updateSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new IllegalArgumentException(LINE_DOES_NOT_EXIST));
        validateSectionStation(sectionRequest.getDownStationId(), line.getStations().getDownStation().getId());
        Station station = stationService.getStation(sectionRequest.getUpStationId());
        sectionService.validateSection(line, station);
        Section section = sectionService.createSection(line, station);
        return SectionResponse.of();
    }

    private void validateSectionStation(long sectionUpStationId, long lineDownStationId) {
        if (sectionUpStationId == lineDownStationId) {
            throw new IllegalArgumentException(STATION_NOT_MATCHED);
        }
    }

    private void validateSection(List<Section> sections, Section section) {

    }

}
