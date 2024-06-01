package com.yangjae.lupine.model.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageDto {
    private int page;
    private int size;
    private int totalCount;
    private int totalPages;
    private int offset;
    private boolean hasNext;
    private boolean hasPrev;
    private int startPage;
    private int endPage;
    private int lastPage;

    public PageDto(int page, int size, int totalCount) {
        setValue();
    }

    public void setValue() {
        this.totalPages = (int) Math.ceil((double) totalCount / size);
        this.offset = ( this.page - 1 ) * this.size;

        this.hasNext = (page < totalPages);
        this.hasPrev = (page > 1);

        // 한 번에 보여줄 최대 페이지 개수
        int maxPageDisplay = 10;

        // startPage 계산
        int tempStartPage = ((page - 1) / maxPageDisplay) * maxPageDisplay + 1;
        this.startPage = Math.max(tempStartPage, 1);

        // endPage 계산
        int tempEndPage = tempStartPage + maxPageDisplay - 1;
        this.endPage = Math.min(tempEndPage, totalPages);

        this.lastPage = totalPages;
    }

    @Builder
    public PageDto(int page, int size, int totalCount, int totalPages, boolean hasNext, boolean hasPrev, int startPage, int endPage, int lastPage) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrev = hasPrev;
        this.startPage = startPage;
        this.endPage = endPage;
        this.lastPage = lastPage;
    }
}
