package subway.subway.domain;

import javax.persistence.*;

public class Station {
    private StationId id;
    private final StationInfo info;

    public static Station register(StationInfo info) {
        return new Station(info);
    }

    private Station(StationInfo info) {
        this.info = info;
    }

    private Station(StationId id, StationInfo info) {
        this.id = id;
        this.info = info;
    }

    public Long getId() {
        if (isNew()) {
            throw new IllegalArgumentException("아직 저장되지 않은 역입니다.");
        }
        return id.getId();
    }

    public String getName() {
        return info.getName();
    }

    public boolean isNew() {
        return id == null;
    }
}
