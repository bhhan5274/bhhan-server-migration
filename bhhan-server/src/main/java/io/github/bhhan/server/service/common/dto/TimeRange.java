package io.github.bhhan.server.service.common.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeRange {
    private String startDate;
    private String endDate;

    @Builder
    public TimeRange(String startDate, String endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeRange timeRange = (TimeRange) o;
        return Objects.equals(startDate, timeRange.startDate) &&
                Objects.equals(endDate, timeRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
