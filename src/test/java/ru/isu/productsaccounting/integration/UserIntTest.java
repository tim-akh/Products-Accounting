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
public class UserIntTest {

    private final String USER_BASE_URL = "/user";

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() throws Exception {
        mvc.perform(post(USER_BASE_URL + "/saveUser").with(csrf())
                        .param("firstName", "fn")
                        .param("lastName", "ln")
                        .param("email", "e@mail.ru")
                        .param("password", "pass")
                        .param("role", "ROLE_ADMIN"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsers() throws Exception {

        mvc.perform(get(USER_BASE_URL + "/listUsers"))
                .andExpect(status().isOk())
                .andExpect(view().name("list_users"))
                .andExpect(model().attributeExists("listUsers"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenGettingAllUsersWithoutAdminRole() throws Exception {
        mvc.perform(get(USER_BASE_URL + "/listUsers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldSaveUser() throws Exception {
        mvc.perform(post(USER_BASE_URL + "/saveUser").with(csrf())
                        .param("firstName", "fn2")
                        .param("lastName", "ln2")
                        .param("email", "e@mail2.ru")
                        .param("password", "pass")
                        .param("role", "ROLE_ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", USER_BASE_URL + "/listUsers"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenSavingUserWithoutAdminRole() throws Exception {
        mvc.perform(post(USER_BASE_URL + "/saveUser").with(csrf())
                        .param("firstName", "fn")
                        .param("lastName", "ln")
                        .param("email", "e@mail.ru")
                        .param("password", "pass")
                        .param("role", "ROLE_ADMIN"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUser() throws Exception {
        mvc.perform(post(USER_BASE_URL + "/updateUser").with(csrf())
                        .param("firstName", "fn0")
                        .param("lastName", "ln0")
                        .param("email", "e@mail0.ru")
                        .param("password", "pass")
                        .param("role", "ROLE_ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", USER_BASE_URL + "/listUsers"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenUpdatingUserWithoutAdminRole() throws Exception {
        mvc.perform(post(USER_BASE_URL + "/saveUser").with(csrf())
                        .param("firstName", "fn")
                        .param("lastName", "ln")
                        .param("email", "e@mail.ru")
                        .param("password", "pass")
                        .param("role", "ROLE_ADMIN"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUser() throws Exception {
        Long id = 1L;

        mvc.perform(get(USER_BASE_URL + "/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", USER_BASE_URL + "/listUsers"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenDeletingUserWithoutAdminRole() throws Exception {
        Long id = 1L;

        mvc.perform(get(USER_BASE_URL + "/delete/" + id))
                .andExpect(status().isForbidden());
    }

}
