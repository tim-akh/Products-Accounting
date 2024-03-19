package ru.isu.productsaccounting.integration;

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

@SpringBootTest
@AutoConfigureMockMvc
public class ReportIntTest {

    private final String REPORT_BASE_URL = "/report";

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ACCOUNTANT")
    void shouldShowReportDatesForm() throws Exception {
        mvc.perform(get(REPORT_BASE_URL + "/createReport"))
                .andExpect(status().isOk())
                .andExpect(view().name("report_form"))
                .andExpect(model().attributeExists("reportDates"));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenShowingReportDatesFormWithoutAccountantRole() throws Exception {
        mvc.perform(get(REPORT_BASE_URL + "/createReport"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ACCOUNTANT")
    void shouldThrowExceptionWhenShowingEmptyReport() throws Exception {
        mvc.perform(post(REPORT_BASE_URL + "/showReport").with(csrf())
                        .param("startDealDate", "2023-04-16")
                        .param("endDealDate", "2023-04-20"))
                .andExpect(result -> assertEquals("В указанный период сделки не производились!",
                        result.getResolvedException().getMessage()));
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenShowingReportWithoutAccountantRole() throws Exception {
        mvc.perform(post(REPORT_BASE_URL + "/showReport").with(csrf())
                        .param("startDealDate", "2022-04-16")
                        .param("endDealDate", "2022-04-20"))
                .andExpect(status().isForbidden());
    }
}
