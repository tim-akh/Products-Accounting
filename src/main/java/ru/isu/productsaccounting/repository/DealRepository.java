package ru.isu.productsaccounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isu.productsaccounting.model.Deal;

import java.util.Date;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    @Query("select deal from Deal deal where deal.dealDate >= ?1 and deal.dealDate <= ?2")
    List<Deal> findAllByDealDateRange(Date startDealDate, Date endDealDate);
}
