package subway.route.service;

import org.springframework.stereotype.Service;
import subway.route.domain.Route;
import subway.route.dto.RouteRequest;
import subway.route.dto.RouteResponse;
import subway.route.repository.RouteRepository;

import javax.transaction.Transactional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Transactional
    public RouteResponse saveRoute(RouteRequest routeRequest) {
        routeRepository.findByName(routeRequest.getName())
                .ifPresent(route -> {
                    throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
                });
        Route route = routeRepository.save(routeRequest.toEntity());
        return new RouteResponse(route);
    }

}
