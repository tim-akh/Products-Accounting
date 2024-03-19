package ru.isu.productsaccounting.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DealIntTest {

    private final String DEAL_BASE_URL = "/deal";

    @Autowired
    private MockMvc mvc;


    @BeforeEach
    void setUp() throws Exception {

        mvc.perform(post("/product/saveProduct").with(csrf())
                .param("name", "Яблоко")
                .param("description", "Краснодарский край"));

        mvc.perform(post(DEAL_BASE_URL + "/saveDeal").with(csrf())
                .param("unit", "кг")
                .param("operation", "Покупка")
                .param("quantity", "5")
                .param("priceForUnit", "80")
                .param("dealDate", "2022-04-18")
                .param("product", "1"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldGetAllDeals() throws Exception {

        mvc.perform(get(DEAL_BASE_URL + "/listDeals"))
                .andExpect(status().isOk())
                .andExpect(view().name("list_deals"))
                .andExpect(model().attributeExists("listDeals"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenGettingAllDealsWithoutManagerRole() throws Exception {
        mvc.perform(get(DEAL_BASE_URL + "/listDeals"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldSaveDeal() throws Exception {
        mvc.perform(post(DEAL_BASE_URL + "/saveDeal").with(csrf())
                        .param("unit", "кг")
                        .param("operation", "Продажа")
                        .param("quantity", "3")
                        .param("priceForUnit", "100")
                        .param("dealDate", "2022-04-19")
                        .param("product", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", DEAL_BASE_URL + "/listDeals"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldThrowExceptionWhenSavingDealWithoutEnoughProducts() throws Exception {
        mvc.perform(post(DEAL_BASE_URL + "/saveDeal").with(csrf())
                        .param("unit", "кг")
                        .param("operation", "Продажа")
                        .param("quantity", "6")
                        .param("priceForUnit", "100")
                        .param("dealDate", "2022-04-19")
                        .param("product", "1"))
                .andExpect(result -> assertEquals("В запасе недостаточно продуктов!",
                        result.getResolvedException().getMessage()));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenSavingDealWithoutManagerRole() throws Exception {
        mvc.perform(post(DEAL_BASE_URL + "/saveDeal").with(csrf())
                        .param("unit", "кг")
                        .param("operation", "Продажа")
                        .param("quantity", "3")
                        .param("priceForUnit", "100")
                        .param("dealDate", "19-04-2022")
                        .param("product", "Яблоко"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldDeleteDeal() throws Exception {
        Long id = 1L;

        mvc.perform(get(DEAL_BASE_URL + "/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", DEAL_BASE_URL + "/listDeals"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenDeletingProductWithoutManagerRole() throws Exception {
        Long id = 1L;

        mvc.perform(get(DEAL_BASE_URL + "/delete/" + id))
                .andExpect(status().isForbidden());
    }
}
