package subway.station.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Station {

    private Long id;
    private String name;

}
