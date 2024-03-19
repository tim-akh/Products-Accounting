package ru.isu.productsaccounting.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.model.Reserve;
import ru.isu.productsaccounting.repository.ReserveRepository;
import ru.isu.productsaccounting.service.ReserveService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReserveServiceTest {

    @Mock
    private ReserveRepository reserveRepository;

    private ReserveService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ReserveService(reserveRepository);
    }

    @Test
    void shouldGetAllReserves() {
        List<Reserve> reserves = new ArrayList<>();

        final Product product = new Product("Яблоко", "");

        reserves.add(new Reserve("кг", 30f, product));
        reserves.add(new Reserve("штука", 100f, product));


        when(reserveRepository.findAll()).thenReturn(reserves);

        List<Reserve> expected = underTest.getAllReserves();
        Assertions.assertEquals(reserves, expected);
    }

    @Test
    void shouldFindReserveByProductAndUnit() {
        final Product product = new Product("Яблоко", "");
        final String unit = "кг";

        final Reserve reserve = new Reserve(unit, 30f, product);

        when(reserveRepository.findReserveByProductAndUnit(product, unit)).thenReturn(reserve);

        final Optional<Reserve> expectedOptional = Optional.ofNullable(underTest
                .findReserveByProductAndUnit(product, unit));

        Assertions.assertNotNull(expectedOptional);
    }

    @Test
    void shouldSaveReserveSuccessfully() {

        final Product product = new Product("Яблоко", "");
        final Reserve reserve = new Reserve("кг", 30f, product);

        underTest.saveReserve(reserve);
        verify(reserveRepository).save(any(Reserve.class));
    }


    @Test
    void shouldDeleteReserve() {
        final Product product = new Product("Яблоко", "");
        final Reserve reserve = new Reserve("кг", 30f, product);

        underTest.deleteReserve(reserve);

        verify(reserveRepository).delete(reserve);
    }
}
