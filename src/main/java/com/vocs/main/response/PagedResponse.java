package com.vocs.main.response;

import java.io.Serializable;
import java.util.List;

public class PagedResponse<T> implements Serializable {
    private int pageNum;
    private int pageSize;
    private long totalPages;
    private long totalCount;
    private List<T> result;

    public PagedResponse() {
    }

    public PagedResponse(Integer pageNum, Integer pageSize, Long totalCount) {
        this.setPageSize(pageSize);
        this.setTotalCount(totalCount);
        this.setPageNum(pageNum);
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        if (pageNum < 1) {
            this.pageNum = 1;
        } else if ((long)pageNum > this.getTotalPages()) {
            this.pageNum = this.getTotalPages().intValue();
        }

    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPages() {
        return this.totalPages;
    }

    private void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        Long result = totalCount / (long)this.getPageSize();
        if (totalCount % (long)this.pageSize != 0L) {
            result = result + 1L;
        }

        this.setTotalPages(result);
    }

    public List<T> getResult() {
        return this.result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
