package testapp.companieslist.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import testapp.companieslist.model.Company;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyControllerTest {
    private static final String URL_TEMPLATE = "/companies";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }
    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        clearDataBase(dataSource);
        fillDataBase(dataSource);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        clearDataBase(dataSource);
    }

    @SneakyThrows
    static void clearDataBase(DataSource dataSource) {
        executeScript(dataSource, "database/remove-companies-from-company-table.sql");
    }

    @SneakyThrows
    static void fillDataBase(DataSource dataSource) {
        executeScript(dataSource, "database/add-companies-to-company-table.sql");
    }

    @SneakyThrows
    static void executeScript(DataSource dataSource, String path) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(path)
            );
        }
    }

    @Test
    void createCompany_validCompany_returnsCompanyResponse() throws Exception {
        Company company = getCompanyOne();
        String jsonRequest = objectMapper.writeValueAsString(company);
        mockMvc.perform(post(URL_TEMPLATE)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(company.getName())))
                .andExpect(jsonPath("$.industry", is(company.getIndustry())))
                .andExpect(jsonPath("$.residence", is(company.getResidence())))
                .andExpect(jsonPath("$.employeeAmount", is(company.getEmployeeAmount())))
                .andExpect(jsonPath("$.capitalization", is(company.getCapitalization())))
                .andReturn();
    }

    @Test
    void getCompanyById_validId_returnsValidCompany() throws Exception {
        Company expected = getCompanyOne();
        long id = 1L;
        mockMvc.perform(get("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(expected.getName())))
                .andExpect(jsonPath("$.industry", is(expected.getIndustry())))
                .andExpect(jsonPath("$.residence", is(expected.getResidence())))
                .andExpect(jsonPath("$.employeeAmount", is(expected.getEmployeeAmount())))
                .andExpect(jsonPath("$.capitalization", is(expected.getCapitalization())))
                .andReturn();
    }

    @Test
    void getCompaniesByIndustry_validCompany_returnsExistingCompany() throws Exception {
        Company goldman = getCompanyOne();
        String industry = "?industry=Capital Markets";
        mockMvc.perform(get("/companies/by-industry" + industry)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.*.name").value(goldman.getName()))
                .andExpect(jsonPath("$.*.industry").value(goldman.getIndustry()))
                .andExpect(jsonPath("$.*.residence").value(goldman.getResidence()))
                .andExpect(jsonPath("$.*.employeeAmount").value(goldman.getEmployeeAmount()))
                .andExpect(jsonPath("$.*.capitalization").value(goldman.getCapitalization()))
                .andReturn();
    }

    @Test
    void updateCompanyById() throws Exception {
        Company expected = getCompanyTwo();
        expected.setIndustry("IT");
        expected.setEmployeeAmount(150000);
        long id = expected.getId();
        String jsonRequest = objectMapper.writeValueAsString(expected);
        mockMvc.perform(put("/companies/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(expected.getName())))
                .andExpect(jsonPath("$.industry", is(expected.getIndustry())))
                .andExpect(jsonPath("$.residence", is(expected.getResidence())))
                .andExpect(jsonPath("$.employeeAmount", is(expected.getEmployeeAmount())))
                .andExpect(jsonPath("$.capitalization", is(expected.getCapitalization())))
                .andReturn();
    }

    @Test
    void deleteCompanyById() throws Exception {
        long id = 1L;
        mockMvc.perform(delete("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Company getCompanyOne() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Goldman Sachs");
        company.setIndustry("Capital Markets");
        company.setResidence("200 West Street New York, NY 10282 United States");
        company.setEmployeeAmount(44400);
        company.setCapitalization(1431540);
        company.setDeleted(false);
        return company;
    }

    private Company getCompanyTwo() {
        Company company = new Company();
        company.setId(2L);
        company.setName("Oracle Corporation");
        company.setIndustry("Software Infrastructure");
        company.setResidence("2300 Oracle Way Austin, TX 78741 United States");
        company.setEmployeeAmount(164000);
        company.setCapitalization(324901000);
        company.setDeleted(false);
        return company;
    }
}
