package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    //특정 노선에 대한 구간 조회
    List<Section> findByLineId(Long lineId);
    void deleteByUpStationId(Long upStationId);
    void deleteAllByLineId(Long lineId);
    //최신 저장한 구간 조회
    Section findTop1ByLineIdOrderByIdDesc(Long lineId);

    //사전 등록 여부 확인
    @Query("FROM Section s WHERE s.lineId =:lineId AND (s.upStationId = :downStationId OR s.downStationId = :downStationId)")
    Section findByLineIdByStationId(Long lineId, Long downStationId);

}
