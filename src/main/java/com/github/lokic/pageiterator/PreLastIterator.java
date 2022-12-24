package com.github.lokic.pageiterator;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class PreLastIterator<T, C> {
    private Integer pageSize;
    private int maxPageNum;
    private BiFunction<List<T>, C, T> computePreLastFunction;
    private NextPageFunction<T, Integer, C, List<T>> nextPageFunction;

    PreLastIterator() {
        this.maxPageNum = Integer.MAX_VALUE;
    }

    public PreLastIterator<T, C> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PreLastIterator<T, C> maxPageNum(int maxPageNum) {
        this.maxPageNum = maxPageNum;
        return this;
    }

    public PreLastIterator<T, C> computePreLast(BiFunction<List<T>, C, T> computePreLastFunction) {
        this.computePreLastFunction = computePreLastFunction;
        return this;
    }

    public PreLastIterator<T, C> nextPage(NextPageFunction<T, Integer, C, List<T>> nextPageFunction) {
        this.nextPageFunction = nextPageFunction;
        return this;
    }


    public Iterator<T> iterator(C ctx) {
        Objects.requireNonNull(pageSize, "pageSize is null");
        Objects.requireNonNull(computePreLastFunction, "computePreLastFunction is null");
        Objects.requireNonNull(nextPageFunction, "nextPageFunction is null");
        PageTask<T, C> pageTask = new PreLastPageTask<T, C>() {
            @Override
            public int getPageSize() {
                return pageSize;
            }

            @Override
            public int getMaxPageNum() {
                return maxPageNum;
            }

            @Override
            protected T computePreLast(List<T> data, C cxt) {
                return computePreLastFunction.apply(data, cxt);
            }

            @Override
            protected List<T> getNextPage(T preLast, int pageSize, C ctx) {
                return nextPageFunction.apply(preLast, pageSize, ctx);
            }
        };
        return PageIterator.iterator(pageTask, ctx);
    }


}
