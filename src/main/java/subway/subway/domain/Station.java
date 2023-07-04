package subway.subway.domain;

public class Station {
    private Station.Id id;
    private final String name;

    public static Station register(String name) {
        return new Station(name);
    }

    private Station(String name) {
        this.name = name;
    }

    public Long getId() {
        if (isNew()) {
            throw new IllegalArgumentException("아직 저장되지 않은 역입니다.");
        }
        return id.getId();
    }

    public String getName() {
        return name;
    }

    public boolean isNew() {
        return id == null;
    }

    public static class Id {
        private final Long id;

        public Id(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
