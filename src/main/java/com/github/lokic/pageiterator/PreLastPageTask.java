package com.github.lokic.pageiterator;

import java.util.List;

/**
 * 基于上次取的最后一条数据取数据
 *
 * @param <T>
 * @param <C>
 */
public abstract class PreLastPageTask<T, C> implements PageTask<T, C> {

    private T start;
    private int currentPageNum = 0;

    @Override
    public List<T> getNextPage(C ctx) {
        currentPageNum = currentPageNum + 1;
        List<T> data = getNextPage(start, getPageSize(), ctx);
        start = computePreLast(data, ctx);
        return data;
    }

    @Override
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    /**
     * 计算上次取的最后一条数据
     *
     * @param data
     * @param cxt
     * @return
     */
    protected abstract T computePreLast(List<T> data, C cxt);

    /**
     * 获取下一页数据
     *
     * @param preLast  是上一次返回的数据列表基于方法{@link PreLastPageTask#computePreLast(List, Object)}计算得出，如果是第一次请求，则preLast为null
     * @param pageSize
     * @param ctx
     * @return
     */
    protected abstract List<T> getNextPage(T preLast, int pageSize, C ctx);

}