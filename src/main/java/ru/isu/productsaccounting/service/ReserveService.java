package ru.isu.productsaccounting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.model.Reserve;
import ru.isu.productsaccounting.repository.ReserveRepository;

import java.util.List;

@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;

    public ReserveService(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    public List<Reserve> getAllReserves() {
        return reserveRepository.findAll();
    }

    public Reserve findReserveByProductAndUnit(Product product, String unit) {
        return reserveRepository.findReserveByProductAndUnit(product, unit);
    }

    public void saveReserve(Reserve reserve) {
        reserveRepository.save(reserve);
    }

    public void deleteReserve(Reserve reserve) {
        reserveRepository.delete(reserve);
    }
}
