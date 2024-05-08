package testapp.companieslist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Table(name = "companies")
@SQLDelete(sql = "UPDATE companies SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String industry;
    @Column(nullable = false)
    private String residence;
    @Column(name = "employee_amount", nullable = false)
    private Integer employeeAmount;
    @Column(nullable = false)
    private Integer capitalization;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
