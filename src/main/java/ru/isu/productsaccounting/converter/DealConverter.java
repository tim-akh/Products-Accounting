package ru.isu.productsaccounting.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import ru.isu.productsaccounting.model.Deal;
import ru.isu.productsaccounting.repository.DealRepository;

import java.util.Optional;

public class DealConverter implements Converter<Long, Optional<Deal>> {

    private final DealRepository dealRepository;

    public DealConverter(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public Optional<Deal> convert(Long id) {
        return dealRepository.findById(id);
    }
}
