package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.code.LineValidateTypeCode;
import subway.line.domain.Line;
import subway.line.domain.Stations;
import subway.line.dto.request.LineRequest;
import subway.line.dto.response.LineResponse;
import subway.line.repository.LineRepository;
import subway.station.repository.StationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private static final String NAME_ALREADY_EXISTS = "이미 존재하는 노선 이름입니다.";
    private static final String ROUTE_DOES_NOT_EXIST = "존재하지 않는 노선입니다.";
    private static final String STATION_DOES_NOT_EXIST = "존재하지 않는 역입니다.";
    private final LineRepository routeRepository;

    private final StationRepository stationRepository;

    public LineService(LineRepository routeRepository, StationRepository stationRepository) {
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveRoute(LineValidateTypeCode routeValidateTypeCode, LineRequest routeRequest) {
        validateRoute(routeValidateTypeCode, routeRequest);
        Line route = routeRequest.toEntity();
        route.saveStations(getStations(routeRequest.getUpStationId(), routeRequest.getDownStationId()));
        return LineResponse.of(routeRepository.save(route));
    }

    private Stations getStations(Long upStationId, Long downStationId) {
        return new Stations(
                stationRepository.findById(upStationId).orElseThrow(() -> new IllegalArgumentException(STATION_DOES_NOT_EXIST)),
                stationRepository.findById(downStationId).orElseThrow(() -> new IllegalArgumentException(STATION_DOES_NOT_EXIST))
        );
    }

    public LineResponse inquiryRoute(Long id) {
        return routeRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(() -> new IllegalArgumentException(ROUTE_DOES_NOT_EXIST));
    }

    public List<LineResponse> inquiryRoutes() {
        return routeRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteRoute(Long id) {
        routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ROUTE_DOES_NOT_EXIST));
        routeRepository.deleteById(id);
    }

    private void validateRoute(LineValidateTypeCode routeValidateTypeCode, LineRequest routeRequest) {
        if (LineValidateTypeCode.UPDATE == routeValidateTypeCode) {
            validateRouteIdAndName(routeRequest);
            return;
        }
        validateRouteName(routeRequest);
    }

    private void validateRouteName(LineRequest routeRequest) {
        routeRepository.findByName(routeRequest.getName())
                .ifPresent(route -> {
                    throw new IllegalArgumentException(NAME_ALREADY_EXISTS);
                });
    }

    private void validateRouteIdAndName(LineRequest routeRequest) {
        routeRepository.findByName(routeRequest.getName())
                .ifPresent(route -> {
                    if (!route.getId().equals(routeRequest.getId())) {
                        throw new IllegalArgumentException(NAME_ALREADY_EXISTS);
                    }
                });
    }

}
