package com.github.lokic.pageiterator;

import java.util.List;

/**
 * 基于页数取数据
 *
 * @param <T>
 * @param <C>
 */
public abstract class PageNumPageTask<T, C> implements PageTask<T, C> {

    private int currentPageNum = 0;

    @Override
    public List<T> getNextPage(C ctx) {
        currentPageNum = currentPageNum + 1;
        return getNextPage(currentPageNum, getPageSize(), ctx);
    }

    @Override
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    abstract List<T> getNextPage(int pageNum, int pageSize, C ctx);
}