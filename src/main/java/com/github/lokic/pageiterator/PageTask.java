package com.github.lokic.pageiterator;

import java.util.List;

public interface PageTask<T, C> {

    List<T> getNextPage(C ctx);

    /**
     * 每页的数据量
     *
     * @return
     */
    int getPageSize();

    /**
     * 最大可以取的页数，从1开始。
     * 某些场景出于性能或执行时间考虑，取特定页数之后还是取不到期望的数据就结束，以避免长时间的轮询
     *
     * @return
     */
    default int getMaxPageNum() {
        return Integer.MAX_VALUE;
    }

    /**
     * 当前页数，如果还没有执行过 {@link PageTask#getNextPage} 则为0
     *
     * @return
     */
    int getCurrentPageNum();

}