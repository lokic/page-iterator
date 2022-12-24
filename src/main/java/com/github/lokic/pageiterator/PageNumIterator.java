package com.github.lokic.pageiterator;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PageNumIterator<T, C> {

    private Integer pageSize;
    private int maxPageNum;
    private NextPageFunction<Integer, Integer, C, List<T>> nextPageFunction;


    PageNumIterator() {
        this.maxPageNum = Integer.MAX_VALUE;
    }

    public PageNumIterator<T, C> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageNumIterator<T, C> maxPageNum(int maxPageNum) {
        this.maxPageNum = maxPageNum;
        return this;
    }

    public PageNumIterator<T, C> nextPage(NextPageFunction<Integer, Integer, C, List<T>> nextPageFunction) {
        this.nextPageFunction = nextPageFunction;
        return this;
    }

    public Iterator<T> iterator(C ctx) {
        Objects.requireNonNull(pageSize, "pageSize is null");
        Objects.requireNonNull(nextPageFunction, "nextPageFunction is null");

        PageTask<T, C> pageTask = new PageNumPageTask<T, C>() {
            @Override
            public int getPageSize() {
                return pageSize;
            }

            @Override
            public int getMaxPageNum() {
                return maxPageNum;
            }

            @Override
            protected List<T> getNextPage(int pageNum, int pageSize, C ctx) {
                return nextPageFunction.apply(pageNum, pageSize, ctx);
            }
        };
        return PageIterator.iterator(pageTask, ctx);
    }
}
