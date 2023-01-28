package subway.route;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station;
import subway.StationRepository;
import subway.StationResponse;
import subway.StationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RouteService {
    private final RouteRepository routeRepository;
    private final StationService stationService;

    public RouteService(RouteRepository routeRepository, StationService stationService) {
        this.routeRepository = routeRepository;
        this.stationService = stationService;
    }

    @Transactional
    public RouteResponse saveRoute(RouteRequest routeRequest) {
        Route route = new Route(routeRequest.getName(), routeRequest.getColor(),
                routeRequest.getUpStationId(), routeRequest.getDownStationId(), routeRequest.getDistance());
        Route newRoute = routeRepository.save(route);
        List<StationResponse> stationResponses = stationService.findAllById(List.of(routeRequest.getUpStationId(), routeRequest.getDownStationId()));
        return new RouteResponse(newRoute.getId(), newRoute.getName(), newRoute.getColor(),stationResponses);
    }
}
