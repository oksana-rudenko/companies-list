package testapp.companieslist.service;

import java.util.List;
import testapp.companieslist.model.Company;

public interface CompanyService {
    Company createCompany(Company company);
    Company getCompanyById(Long id);
    List<Company> getCompaniesByIndustry(String industry);
    Company updateCompanyById(Long id, Company company);
    void deleteCompanyById(Long id);
}
