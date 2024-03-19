package ru.isu.productsaccounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.model.Reserve;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    Reserve findReserveByProductAndUnit(Product product, String unit);
}
