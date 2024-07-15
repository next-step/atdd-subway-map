package subway.domain.entity.line;

import lombok.NonNull;
import subway.domain.exception.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class LineSections implements Iterable<LineSection> {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineSection> data = new ArrayList<>();

    protected void addSection(LineSection section) {
        // 새로운 구간의 상행역이 노선의 하행종창역이 아니도록 구간인 경우 에러
        if (!data.isEmpty() && !getLastSection().getDownStationId().equals(section.getUpStationId())) {
            throw new InvalidUpStationException(section.getUpStationId());
        }

        // 새로운 구간의 하행역이 이미 노선에 포함되어 있는 경우 에러
        verifyDownStationAlreadyExisted(section.getDownStationId());

        this.data.add(section);
    }

    private void verifyDownStationAlreadyExisted(Long downStationId) {
        if (getAllStationIds().stream().anyMatch(existedStationId -> existedStationId.equals(downStationId))) {
            throw new InvalidDownStationException(downStationId);
        }
    }

    protected void deleteSection(Long stationId) {
        if (size() <= 1) {
            throw new SubwayDomainException(SubwayDomainExceptionType.INVALID_SECTION_SIZE);
        }

        // 삭제할 역이 노선의 하행종창역이 아닌 경우 에러
        if (!getLastSection().getDownStationId().equals(stationId)) {
            throw new InvalidStationException(stationId);
        }

        // 마지막 section 에서 제거
        data.remove(size() -1);
    }

    public Stream<LineSection> stream() {
        return data.stream();
    }

    public int size() {
        return data.size();
    }

    public LineSection getLastSection() {
        return data.isEmpty() ? null : data.get(size() - 1);
    }

    private List<Long> getAllUpStationIds() {
        return data.stream()
                .map(LineSection::getUpStationId)
                .collect(Collectors.toList());
    }

    public List<Long> getAllStationIds() {
        // 모든 상행선 가져오기
        List<Long> stationIds = getAllUpStationIds();

        // 마지막 하행선 추가
        if (getLastSection() != null) {
            stationIds.add(getLastSection().getDownStationId());
        }
        return stationIds;
    }

    @Override
    @NonNull
    public Iterator<LineSection> iterator() {
        return data.iterator();
    }
}
