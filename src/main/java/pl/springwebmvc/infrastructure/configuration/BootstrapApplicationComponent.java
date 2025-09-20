package pl.springwebmvc.infrastructure.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.springwebmvc.infrastructure.database.entity.EmployeeEntity;
import pl.springwebmvc.infrastructure.database.repository.EmployeeRepository;

import java.math.BigDecimal;

@Slf4j
@Component
@AllArgsConstructor
public class BootstrapApplicationComponent implements ApplicationListener<ContextRefreshedEvent> {

    private EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final @NonNull ContextRefreshedEvent event) {
        employeeRepository.deleteAll();

        employeeRepository.save(EmployeeEntity.builder()
                .name("Stefan")
                .surname("Zajavka")
                .salary(new BigDecimal("7400.00"))
                .phone("+48 501 111 222")
                .email("stefan@zajavka.pl")
                .build());
        employeeRepository.save(EmployeeEntity.builder()
                .name("Agnieszka")
                .surname("Spring")
                .salary(new BigDecimal("6200.00"))
                .phone("+48 501 222 333")
                .email("agnieszka@zajavka.pl")
                .build());
        employeeRepository.save(EmployeeEntity.builder()
                .name("Tomasz")
                .surname("Hibernate")
                .salary(new BigDecimal("5300.00"))
                .phone("+48 501 333 444")
                .email("tomasz@zajavka.pl")
                .build());
    }
}
