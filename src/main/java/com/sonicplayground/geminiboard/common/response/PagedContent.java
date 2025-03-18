package com.sonicplayground.geminiboard.common.response;


import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PagedContent<T> {
    private final long totalElements; // 총 요소 개수
    private final long totalPages; // 총 페이지 개수
    private final int size; // 페이지 당 요소 개수
    private final int number; // 현재 페이지
    private final List<T> content;

    public PagedContent(Page<T> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.content = page.getContent();
        this.size = page.getSize();
        this.number = page.getNumber();
    }
}
