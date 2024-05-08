package testapp.companieslist.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import testapp.companieslist.model.Company;
import testapp.companieslist.repository.CompanyRepository;
import testapp.companieslist.service.CompanyService;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Company by id: " + id + " is not present in DB")
        );
    }

    @Override
    public List<Company> getCompaniesByIndustry(String industry) {
        return companyRepository.findCompaniesByIndustry(industry);
    }

    @Override
    public Company updateCompanyById(Long id, Company company) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Can't update company by id:" + id
            + " as it does not exist");
        }
        company.setId(id);
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompanyById(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new  RuntimeException("Company by id: " + id
            + " does not exist and can't be deleted");
        }
        companyRepository.deleteById(id);
    }
}
