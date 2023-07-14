package subway.route.service;

import org.springframework.stereotype.Service;
import subway.route.code.RouteValidateTypeCode;
import subway.route.domain.Route;
import subway.route.domain.Stations;
import subway.route.dto.RouteRequest;
import subway.route.dto.RouteResponse;
import subway.route.repository.RouteRepository;
import subway.station.repository.StationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteService {

    private static final String NAME_ALREADY_EXISTS = "이미 존재하는 노선 이름입니다.";
    private static final String ROUTE_DOES_NOT_EXIST = "존재하지 않는 노선입니다.";
    private static final String STATION_DOES_NOT_EXIST = "존재하지 않는 역입니다.";
    private final RouteRepository routeRepository;

    private final StationRepository stationRepository;

    public RouteService(RouteRepository routeRepository, StationRepository stationRepository) {
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
    }

    public RouteResponse saveRoute(RouteValidateTypeCode routeValidateTypeCode, RouteRequest routeRequest) {
        validateRoute(routeValidateTypeCode, routeRequest);
        Route route = routeRequest.toEntity();
        route.saveStations(getStations(routeRequest.getUpStationId(), routeRequest.getDownStationId()));
        return RouteResponse.of(routeRepository.save(route));
    }

    private Stations getStations(Long upStationId, Long downStationId) {
        return new Stations(
                stationRepository.findById(upStationId).orElseThrow(() -> new IllegalArgumentException(STATION_DOES_NOT_EXIST)),
                stationRepository.findById(downStationId).orElseThrow(() -> new IllegalArgumentException(STATION_DOES_NOT_EXIST))
        );
    }

    public RouteResponse inquiryRoute(Long id) {
        return routeRepository.findById(id)
                .map(RouteResponse::of)
                .orElseThrow(() -> new IllegalArgumentException(ROUTE_DOES_NOT_EXIST));
    }

    public List<RouteResponse> inquiryRoutes() {
        return routeRepository.findAll().stream()
                .map(RouteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteRoute(Long id) {
        routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ROUTE_DOES_NOT_EXIST));
        routeRepository.deleteById(id);
    }

    private void validateRoute(RouteValidateTypeCode routeValidateTypeCode, RouteRequest routeRequest) {
        if (RouteValidateTypeCode.UPDATE == routeValidateTypeCode) {
            validateRouteIdAndName(routeRequest);
            return;
        }
        validateRouteName(routeRequest);
    }

    private void validateRouteName(RouteRequest routeRequest) {
        routeRepository.findByName(routeRequest.getName())
                .ifPresent(route -> {
                    throw new IllegalArgumentException(NAME_ALREADY_EXISTS);
                });
    }

    private void validateRouteIdAndName(RouteRequest routeRequest) {
        routeRepository.findByName(routeRequest.getName())
                .ifPresent(route -> {
                    if (!route.getId().equals(routeRequest.getId())) {
                        throw new IllegalArgumentException(NAME_ALREADY_EXISTS);
                    }
                });
    }

}
