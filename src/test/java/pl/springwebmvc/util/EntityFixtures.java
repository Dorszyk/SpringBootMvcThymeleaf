package pl.springwebmvc.util;

import lombok.experimental.UtilityClass;
import pl.springwebmvc.infrastructure.database.entity.EmployeeEntity;

import java.math.BigDecimal;

@UtilityClass
public class EntityFixtures {
    public static EmployeeEntity someEmployee1(){
        return EmployeeEntity.builder()
                .name("Agnieszka")
                .surname("Spring")
                .salary(new BigDecimal("62341.00"))
                .phone("501 222 333")
                .email("agnieszka@zajavka.pl")
                .build();
    }
    public static EmployeeEntity someEmployee2(){
        return EmployeeEntity.builder()
                .name("Stefan")
                .surname("Zajavka")
                .salary(new BigDecimal("52322.00"))
                .phone("501 111 222")
                .email("stefan@zajavka.pl")
                .build();
    }

    public static EmployeeEntity someEmployee3(){
        return EmployeeEntity.builder()
                .name("Tomasz")
                .surname("Hibernate")
                .salary(new BigDecimal("53231.00"))
                .phone("501 333 444")
                .email("tomasz@zajavka.pl")
                .build();
    }
}

