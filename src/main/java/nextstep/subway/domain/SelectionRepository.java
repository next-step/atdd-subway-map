package nextstep.subway.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionRepository extends JpaRepository<Selection, Long> {
	List<Selection> findByLineIdOrderById(long lineId);
}