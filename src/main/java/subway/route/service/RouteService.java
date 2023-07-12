package subway.route.service;

import org.springframework.stereotype.Service;
import subway.route.code.RouteValidateTypeCode;
import subway.route.domain.Route;
import subway.route.dto.RouteRequest;
import subway.route.dto.RouteResponse;
import subway.route.repository.RouteRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteService {

    private static final String NAME_ALREADY_EXISTS = "이미 존재하는 노선 이름입니다.";
    private static final String NAME_DOES_NOT_EXIST = "존재하지 않는 노선입니다.";
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public RouteResponse saveRoute(RouteValidateTypeCode routeValidateTypeCode, RouteRequest routeRequest) {
        validateRoute(routeValidateTypeCode, routeRequest);
        Route route = routeRepository.save(routeRequest.toEntity());
        return new RouteResponse(route);
    }

    public RouteResponse inquiryRoute(Long id) {
        return routeRepository.findById(id)
                .map(RouteResponse::new)
                .orElseThrow(() -> new IllegalArgumentException(NAME_DOES_NOT_EXIST));
    }

    public List<RouteResponse> inquiryRoutes() {
        return routeRepository.findAll().stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteRoute(Long id) {
        routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NAME_DOES_NOT_EXIST));
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
