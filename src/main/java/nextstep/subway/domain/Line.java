package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.enums.Direction;
import nextstep.subway.exception.InvalidEndSectionException;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.exception.OutOfSectionSizeException;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(Long id, LineRequest lineRequest) {
        return new Line(id, lineRequest.getName(), lineRequest.getColor());
    }

    public static Line of(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean addSection(Section section) {
        // section 이 상행종점인지, 하행 종목인지를 구분한다.
        Direction direction = getStations()
                                .getDirection(section);
        if (direction.equals(Direction.NEW)
        || (direction.equals(Direction.DOWN) && validateDownEnd(section.getUpStation()))
        || (direction.equals(Direction.UP) && validateUpEnd(section.getDownStation()))) {
            return this.sections.add(section);
        }

        return false;
    }

    public void deleteSection(Station station) {
        validateDeleteMinSize();
        validateStationExist(station);
        Optional<Section> endSectionByStation = getEndSectionByStation(station);

        if (endSectionByStation.isEmpty()) {
            throw new InvalidEndSectionException();
        }

         // 삭제
        this.sections.stream()
                .filter(section -> section.hasStation(station))
                .findAny()
                .ifPresent(sections::remove);
    }

    /**
     * station 이 상행 혹은 하행종점이 아니면 에러
     * @param station
     */
    private Optional<Section> getEndSectionByStation(Station station) {
        Optional<Section> upEndByStation = getUpEndByStation(station);
        if (upEndByStation.isPresent()) {
            return upEndByStation;
        }

        return getDownEndByStation(station);
    }

    /**
     * station 이 있는지 조사 없으면 에러
     */
    private void validateStationExist(Station station) {
        if (!getStations().contains(station)) {
            throw new NotFoundStationException(station);
        }
    }

    /**
     * sections 이 size 가 1 이면 에러
     */
    public void validateDeleteMinSize() {
        if (this.sections.size() == 1) {
            throw new OutOfSectionSizeException();
        }
    }

    private Optional<Section> getUpEndByStation(Station station) {
        List<Section> stations = sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .collect(Collectors.toList());
        boolean b = sections.stream()
                .noneMatch(section -> section.getDownStation().equals(station));

        if (stations.size() == 1 && b) {
            return Optional.of(stations.get(0));
        }

        return Optional.empty();
    }

    private boolean validateUpEnd(Station station) {
        if (getUpEndByStation(station).isEmpty()) {
            throw new InvalidEndSectionException();
        }

        return true;
    }

    private Optional<Section> getDownEndByStation(Station station) {
        List<Section> stations = sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .collect(Collectors.toList());
        boolean b = sections.stream()
                .noneMatch(section -> section.getUpStation().equals(station));

        if (stations.size() == 1 && b) {
            return Optional.of(stations.get(0));
        }

        return Optional.empty();
    }

    private boolean validateDownEnd(Station station) {
        if (getDownEndByStation(station).isEmpty()) {
            throw new InvalidEndSectionException();
        }

        return true;
    }

    public Stations getStations() {
        LinkedList<Station> list = new LinkedList<>();
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            if (list.isEmpty()) {
                list.addLast(upStation);
                list.addLast(downStation);
                continue;
            }

            if (list.indexOf(upStation) == -1) {
                list.addFirst(upStation);
                continue;
            }

            if (list.indexOf(downStation) == -1) {
                list.add(downStation);
            }
//
//            if (upStationIndex != -1 ) {
//                list.add(upStationIndex + 1, upStation);
//            }
//
        }

        List<Station> stations = new ArrayList<>(list);
        return Stations.of(stations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }
}
