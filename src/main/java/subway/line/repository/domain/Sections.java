package subway.line.repository.domain;

import subway.line.exception.SectionConnectException;
import subway.line.exception.SectionDisconnectException;
import subway.station.repository.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Embeddable
public class Sections implements Iterable<Section> {
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "section_id")
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    @Override
    public Iterator<Section> iterator() {
        return sections.iterator();
    }

    public Stream<Section> stream() {
        return sections.stream();
    }

    public void connect(final Section section) {
        validateSectionConnection(section);
        this.sections.add(section);
    }

    public int getLastSectionDistance() {
        return getLastSection()
                .map(Section::getDistance)
                .orElse(0);
    }

    public void disconnectLastSection(final Station station) {
        if(sections.size() <= 1) {
            throw new SectionDisconnectException("더이상 구간을 제거할 수 없습니다.");
        }

        this.sections.remove(sections.size() - 1);
    }

    private void validateSectionConnection(final Section section) {
        if(sections.isEmpty()) {
            return;
        }

        if (containsStation(section.getDownStation())) {
            throw new SectionConnectException("생성할 구간 하행역이 해당 노선에 이미 등록되어 있습니다.");
        }

        if (canConnectToLastDownStation(section)) {
            throw new SectionConnectException("생성할 구간 상행역이 해당 노선의 하행 종점역이 아닙니다.");
        }

    }

    private boolean containsStation(final Station station) {
        return sections.stream()
                .anyMatch(row -> row.contains(station));
    }

    private boolean canConnectToLastDownStation(final Section section) {
        return getLastDownStation().stream().noneMatch(station -> station.equals(section.getUpStation()));
    }

    private Optional<Station> getLastDownStation() {
        return getLastSection().map(Section::getDownStation);
    }

    private Optional<Section> getLastSection() {
        if(sections.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(sections.get(sections.size() - 1));
    }
}
