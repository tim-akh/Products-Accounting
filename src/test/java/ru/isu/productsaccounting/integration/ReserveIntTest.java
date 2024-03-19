package ru.isu.productsaccounting.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReserveIntTest {

    private final String RESERVE_BASE_URL = "/reserve";

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldGetAllReserves() throws Exception {

        mvc.perform(get(RESERVE_BASE_URL + "/listReserves"))
                .andExpect(status().isOk())
                .andExpect(view().name("list_reserves"))
                .andExpect(model().attributeExists("listReserves"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenGettingAllReservesWithoutManagerOrAccountantRole() throws Exception {
        mvc.perform(get(RESERVE_BASE_URL + "/listProducts"))
                .andExpect(status().isForbidden());
    }
}
