package testapp.companieslist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testapp.companieslist.model.Company;
import testapp.companieslist.repository.CompanyRepository;
import testapp.companieslist.service.impl.CompanyServiceImpl;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {
    private static final Company GOLDMAN_COMPANY = new Company();
    private static final String CAPITAL_INDUSTRY = "Capital Markets";
    private static final String INVALID_INDUSTRY = "Production";
    private static final Long COMPANY_ID = 1L;
    private static final Long INVALID_ID = -1L;
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyServiceImpl companyService;

    @BeforeAll
    static void beforeAll() {
        GOLDMAN_COMPANY.setId(1L);
        GOLDMAN_COMPANY.setName("Goldman Sachs");
        GOLDMAN_COMPANY.setIndustry("Capital Markets");
        GOLDMAN_COMPANY.setResidence("200 West Street New York, NY 10282 United States");
        GOLDMAN_COMPANY.setEmployeeAmount(44400);
        GOLDMAN_COMPANY.setCapitalization(1431540);
        GOLDMAN_COMPANY.setDeleted(false);
    }

    @Test
    void createCompany_validCompany_returnsValidCompany() {
        when(companyRepository.save(GOLDMAN_COMPANY)).thenReturn(GOLDMAN_COMPANY);
        Company actual = companyService.createCompany(GOLDMAN_COMPANY);
        Assertions.assertEquals(GOLDMAN_COMPANY, actual);
        verify(companyRepository, times(1)).save(GOLDMAN_COMPANY);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void getCompanyById_validId_returnsValidCompany() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(GOLDMAN_COMPANY));
        Company actual = companyService.getCompanyById(COMPANY_ID);
        Assertions.assertEquals(GOLDMAN_COMPANY, actual);
        verify(companyRepository, times(1)).findById(COMPANY_ID);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void getCompanyById_invalidId_returnsException() {
        when(companyRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> companyService.getCompanyById(INVALID_ID)
        );
        String expected = "Company by id: " + INVALID_ID + " is not present in DB";
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
        verify(companyRepository, times(1)).findById(INVALID_ID);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void getCompaniesByIndustry_validIndustry_returnsListOfOne() {
        List<Company> companies = Collections.singletonList(GOLDMAN_COMPANY);
        when(companyRepository.findCompaniesByIndustry(CAPITAL_INDUSTRY)).thenReturn(companies);
        List<Company> actual = companyService.getCompaniesByIndustry(CAPITAL_INDUSTRY);
        Assertions.assertEquals(companies, actual);
        verify(companyRepository, times(1)).findCompaniesByIndustry(CAPITAL_INDUSTRY);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void getCompaniesByIndustry_invalidIndustry_returnsEmptyList() {
        List<Company> companies = Collections.emptyList();
        when(companyRepository.findCompaniesByIndustry(INVALID_INDUSTRY)).thenReturn(companies);
        List<Company> actual = companyService.getCompaniesByIndustry(INVALID_INDUSTRY);
        Assertions.assertEquals(0, actual.size());
        verify(companyRepository, times(1)).findCompaniesByIndustry(INVALID_INDUSTRY);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void updateCompanyById_validId_returnsUpdatedCompany() {
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(true);
        when(companyRepository.save(GOLDMAN_COMPANY)).thenReturn(GOLDMAN_COMPANY);
        Company actual = companyService.updateCompanyById(COMPANY_ID, GOLDMAN_COMPANY);
        Assertions.assertEquals(GOLDMAN_COMPANY, actual);
        verify(companyRepository, times(1)).existsById(COMPANY_ID);
        verify(companyRepository, times(1)).save(GOLDMAN_COMPANY);
    }

    @Test
    void updateCompanyById_invalidId_returnsUpdatedCompany() {
        when(companyRepository.existsById(INVALID_ID)).thenReturn(false);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> companyService.updateCompanyById(INVALID_ID, GOLDMAN_COMPANY)
        );
        String expected = "Can't update company by id:" + INVALID_ID + " as it does not exist";
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
        verify(companyRepository, times(1)).existsById(any());
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void deleteCompanyById_validId_softDelete() {
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(true);
        companyService.deleteCompanyById(COMPANY_ID);
        verify(companyRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void deleteCompanyById_invalidId_returnsException() {
        when(companyRepository.existsById(INVALID_ID)).thenReturn(false);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> companyService.deleteCompanyById(INVALID_ID)
        );
        String expected = "Company by id: " + INVALID_ID + " does not exist and can't be deleted";
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
        verify(companyRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(companyRepository);
    }
}
