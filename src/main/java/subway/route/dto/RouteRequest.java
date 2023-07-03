package subway.route.dto;

import subway.route.domain.Route;

public class RouteRequest {

    private String name;

    public Route toEntity() {
        return new Route(name);
    }

    public String getName() {
        return name;
    }

}
