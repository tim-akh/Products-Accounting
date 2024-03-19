package ru.isu.productsaccounting.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.isu.productsaccounting.model.Deal;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.repository.DealRepository;
import ru.isu.productsaccounting.service.DealService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {

    @Mock
    private DealRepository dealRepository;

    private DealService underTest;

    @BeforeEach
    void setUp() {
        underTest = new DealService(dealRepository);
    }

    @Test
    void shouldGetAllDeals() {

        final Product product = new Product("Яблоко", "");

        List<Deal> deals = new ArrayList<>();

        deals.add(new Deal(1L, "кг", "Покупка", 5f, 80f,
                new Date(2022, Calendar.APRIL, 12), product));
        deals.add(new Deal(2L, "кг", "Продажа", 3f, 100f,
                new Date(2022, Calendar.APRIL, 15), product));

        when(dealRepository.findAll()).thenReturn(deals);

        List<Deal> expected = underTest.getAllDeals();
        Assertions.assertEquals(deals, expected);
    }

    @Test
    void findDealsByDealDateRange() {

        final Product product = new Product("Яблоко", "");

        List<Deal> deals = new ArrayList<>();

        deals.add(new Deal(1L, "кг", "Покупка", 5f, 80f,
                new Date(2022, Calendar.APRIL, 12), product));
        deals.add(new Deal(2L, "кг", "Продажа", 3f, 100f,
                new Date(2022, Calendar.APRIL, 15), product));

        final Date startDealDate = new Date(2022, Calendar.APRIL, 10);
        final Date endDealDate = new Date(2022, Calendar.APRIL, 20);

        when(dealRepository.findAllByDealDateRange(startDealDate, endDealDate))
                .thenReturn(deals);

        final Optional<List<Deal>> expectedOptional = Optional.ofNullable(underTest.
                findAllByDealDateRange(startDealDate, endDealDate));

        Assertions.assertNotNull(expectedOptional);
    }

    @Test
    void shouldSaveDealSuccessfully() {

        final Product product = new Product("Яблоко", "");

        final Deal deal = new Deal(1L, "кг", "Покупка", 5f, 80f,
                new Date(2022, Calendar.APRIL, 12), product);

        underTest.saveDeal(deal);
        verify(dealRepository).save(any(Deal.class));
    }


    @Test
    void shouldDeleteDeal() {

        final Product product = new Product("Яблоко", "");

        final Deal deal = new Deal(1L, "кг", "Покупка", 5f, 80f,
                new Date(2022, Calendar.APRIL, 12), product);

        underTest.deleteDeal(deal);

        verify(dealRepository).delete(deal);
    }
}
