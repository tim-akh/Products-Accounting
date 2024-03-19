package ru.isu.productsaccounting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.productsaccounting.model.Deal;
import ru.isu.productsaccounting.repository.DealRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DealService {

    private final DealRepository dealRepository;

    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public void saveDeal(Deal deal) {
        dealRepository.save(deal);
    }

    public void deleteDeal(Deal deal) {
        dealRepository.delete(deal);
    }

    public List<Deal> findAllByDealDateRange(Date startDealDate, Date endDealDate) {
        return dealRepository.findAllByDealDateRange(startDealDate, endDealDate);
    }
}
