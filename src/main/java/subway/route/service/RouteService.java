package subway.route.service;

import org.springframework.stereotype.Service;
import subway.route.domain.Route;
import subway.route.dto.RouteRequest;
import subway.route.dto.RouteResponse;
import subway.route.repository.RouteRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public RouteResponse saveRoute(RouteRequest routeRequest) {
        routeRepository.findByName(routeRequest.getName())
                .ifPresent(route -> {
                    throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
                });
        Route route = routeRepository.save(routeRequest.toEntity());
        return new RouteResponse(route);
    }

    public RouteResponse inquiryRoute(Long id) {
        return routeRepository.findById(id)
                .map(RouteResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }

    public List<RouteResponse> inquiryRoutes() {
        return routeRepository.findAll().stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    public RouteResponse updateRoute(RouteRequest routeRequest) {
        return saveRoute(routeRequest);
    }

    public void deleteRoute(Long id) {
        routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        routeRepository.deleteById(id);
    }
}
