package com.github.lokic.pageiterator;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;
import java.util.List;

public class PageIterator<T, C> extends AbstractIterator<List<T>> {
    private boolean isEnd;
    private final PageTask<T, C> pageTask;
    private final C ctx;

    private PageIterator(PageTask<T, C> pageTask, C ctx) {
        this.isEnd = false;
        this.pageTask = pageTask;
        this.ctx = ctx;
    }

    @Override
    protected List<T> computeNext() {
        if (isEnd) {
            return endOfData();
        }
        List<T> data = pageTask.getNextPage(ctx);
        if (data.size() < pageTask.getPageSize()
                || pageTask.getCurrentPageNum() >= pageTask.getMaxPageNum()) {
            isEnd = true;
        }
        return data;
    }

    private static class PageItemIterator<T> extends AbstractIterator<T> {

        private final Iterator<List<T>> pageIterator;

        private Iterator<T> itemIterator;

        private PageItemIterator(Iterator<List<T>> pageIterator) {
            this.pageIterator = pageIterator;
        }

        @Override
        protected T computeNext() {
            if (itemIterator == null || !itemIterator.hasNext()) {
                itemIterator = computeNextItemIterator();
                if (itemIterator == null) {
                    return null;
                }
            }
            if (itemIterator.hasNext()) {
                return itemIterator.next();
            }
            return computeNext();
        }


        private Iterator<T> computeNextItemIterator() {
            if (!pageIterator.hasNext()) {
                endOfData();
                return null;
            }
            return pageIterator.next().iterator();
        }

    }


    public static <T, C> Iterator<T> iterator(PageTask<T, C> task, C ctx) {
        PageIterator<T, C> it = new PageIterator<>(task, ctx);
        return new PageItemIterator<>(it);
    }

    public static <T, C> PageNumIterator<T, C> pageNumIterator() {
        return new PageNumIterator<>();
    }

    public static <T, C> PreLastIterator<T, C> preLastIterator() {
        return new PreLastIterator<>();
    }
}
