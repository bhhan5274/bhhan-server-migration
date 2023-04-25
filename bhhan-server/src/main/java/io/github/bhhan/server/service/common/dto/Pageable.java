package io.github.bhhan.server.service.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pageable {
    private static final int DEFAULT_SIZE = 6;
    private static final int MAX_SIZE = 50;
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    @Min(value = 1, message = "최소 size는 1입니다.")
    private int size;

    @Min(value = 1, message = "최소 page는 1입니다.")
    private int page;

    private List<String> sort = new ArrayList<>();

    @Builder
    public Pageable(int page, int size, List<String> sort){
        this.page = page;
        this.size = size;

        if(Objects.nonNull(sort)){
            this.sort.addAll(sort);
        }
    }

    public void setSize(int size) {
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public org.springframework.data.domain.Pageable of(){
        return sort.isEmpty() ? PageRequest.of(page - 1, size) : PageRequest.of(page - 1, size, makeSort());
    }

    private Sort makeSort() {
        List<Sort.Order> orders = new ArrayList<>();

        if(!sort.get(0).contains(",")){
            parseSizeOne(orders);
        }else {
            parseSizeOthers(orders);
        }

        return Sort.by(orders);
    }

    private void parseSizeOne(List<Sort.Order> orders) {
        String order = sort.get(1);
        String property = sort.get(0);

        addOrderProperty(orders, order, property);
    }

    private void parseSizeOthers(List<Sort.Order> orders) {
        for (String str : sort) {
            String[] split = str.split(",");
            addOrderProperty(orders, split[1], split[0]);
        }
    }

    private void addOrderProperty(List<Sort.Order> orders, String order, String property) {
        if (order.equalsIgnoreCase(ASC)) {
            orders.add(Sort.Order.asc(property));
        } else if (order.equalsIgnoreCase(DESC)) {
            orders.add(Sort.Order.desc(property));
        } else {
            throw new IllegalArgumentException("Pageable Sort Parsing Error!!!");
        }
    }
}
