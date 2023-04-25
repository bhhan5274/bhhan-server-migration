package io.github.bhhan.server.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {
    @Test
    @DisplayName("validateDate_success")
    void validDate_periodCreated(){
        LocalDate startDate = LocalDate.of(2021, 1, 10);
        LocalDate endDate = LocalDate.of(2021, 1, 11);

        Period period = Period.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        assertNotNull(period);
    }

    @Test
    @DisplayName("validateDate_fail")
    void invalidDate_periodThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            LocalDate startDate = LocalDate.of(2021, 1, 11);
            LocalDate endDate = LocalDate.of(2021, 1, 10);

            Period.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        });
    }
}
