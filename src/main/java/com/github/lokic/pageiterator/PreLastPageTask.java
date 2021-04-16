package com.github.lokic.pageiterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于上次取的最后一条数据取数据
 * @param <T>
 * @param <C>
 */
public abstract class PreLastPageTask<T, C> implements PageTask<T, C> {

    private T start;
    private int currentPageNum = 0;

    @Override
    public List<T> getNextPage(C ctx) {
        currentPageNum = currentPageNum + 1;
        List<T> data = getNextData(start, getPageSize(), ctx);
        start = computePreLast(data, ctx);

        return data;
    }

    @Override
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    /**
     * 计算上次取的最后一条数据
     * @param data
     * @param cxt
     * @return
     */
    abstract T computePreLast(List<T> data, C cxt);

    abstract List<T> getNextData(T preLast , int pageSize, C ctx);

}