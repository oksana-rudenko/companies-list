package testapp.companieslist.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import testapp.companieslist.model.Company;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyRepositoryTest {
    private static final String SOFTWARE_INDUSTRY = "Software Infrastructure";
    private static final String CAPITAL_INDUSTRY = "Capital Markets";
    private static final String INVALID_INDUSTRY = "Production";
    @Autowired
    private CompanyRepository companyRepository;

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/remove-companies-from-company-table.sql"
                    )
            );
        }
    }

    @Test
    @Sql(scripts = {
            "classpath:database/remove-companies-from-company-table.sql",
            "classpath:database/add-companies-to-company-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findCompaniesByIndustry_validValue_returnsListOfTwoCompanies() {
        Company oracle = getCompanyTwo();
        Company fortinet = getCompanyThree();
        List<Company> expected = new ArrayList<>();
        expected.add(oracle);
        expected.add(fortinet);
        List<Company> actual = companyRepository.findCompaniesByIndustry(SOFTWARE_INDUSTRY);
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {
            "classpath:database/remove-companies-from-company-table.sql",
            "classpath:database/add-companies-to-company-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findCompaniesByIndustry_validValue_returnsListOfOneCompany() {
        Company goldmanSachs = getCompanyOne();
        List<Company> expected = new ArrayList<>();
        expected.add(goldmanSachs);
        List<Company> actual = companyRepository.findCompaniesByIndustry(CAPITAL_INDUSTRY);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {
            "classpath:database/remove-companies-from-company-table.sql",
            "classpath:database/add-companies-to-company-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findCompaniesByIndustry_validValue_returnsEmptyList() {
        List<Company> actual = companyRepository.findCompaniesByIndustry(INVALID_INDUSTRY);
        Assertions.assertEquals(0, actual.size());
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

    private Company getCompanyThree() {
        Company company = new Company();
        company.setId(3L);
        company.setName("Fortinet");
        company.setIndustry("Software Infrastructure");
        company.setResidence("909 Kifer Road Sunnyvale, CA 94086 United States");
        company.setEmployeeAmount(13568);
        company.setCapitalization(45151000);
        company.setDeleted(false);
        return company;
    }
}
