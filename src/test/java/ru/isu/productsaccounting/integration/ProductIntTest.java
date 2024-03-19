package ru.isu.productsaccounting.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntTest {

    private final String PRODUCT_BASE_URL = "/product";

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() throws Exception {
        mvc.perform(post(PRODUCT_BASE_URL + "/saveProduct").with(csrf())
                .param("name", "name")
                .param("description", "description"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldGetAllProducts() throws Exception {

        mvc.perform(get(PRODUCT_BASE_URL + "/listProducts"))
                .andExpect(status().isOk())
                .andExpect(view().name("list_products"))
                .andExpect(model().attributeExists("listProducts"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenGettingAllProductsWithoutManagerRole() throws Exception {
        mvc.perform(get(PRODUCT_BASE_URL + "/listProducts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldSaveProduct() throws Exception {
        mvc.perform(post(PRODUCT_BASE_URL + "/saveProduct").with(csrf())
                        .param("name", "name2")
                        .param("description", "description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", PRODUCT_BASE_URL + "/listProducts"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenSavingProductWithoutManagerRole() throws Exception {
        mvc.perform(post(PRODUCT_BASE_URL + "/saveProduct").with(csrf())
                        .param("name", "name2")
                        .param("description", "description"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldUpdateProduct() throws Exception {
        mvc.perform(post(PRODUCT_BASE_URL + "/updateProduct").with(csrf())
                        .param("name", "name")
                        .param("description", "description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", PRODUCT_BASE_URL + "/listProducts"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenUpdatingProductWithoutManagerRole() throws Exception {
        mvc.perform(post(PRODUCT_BASE_URL + "/saveProduct").with(csrf())
                        .param("name", "name2")
                        .param("description", "description"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldDeleteProduct() throws Exception {
        Long id = 1L;

        mvc.perform(get(PRODUCT_BASE_URL + "/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", PRODUCT_BASE_URL + "/listProducts"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenDeletingProductWithoutManagerRole() throws Exception {
        Long id = 1L;

        mvc.perform(get(PRODUCT_BASE_URL + "/delete/" + id))
                .andExpect(status().isForbidden());
    }
}
