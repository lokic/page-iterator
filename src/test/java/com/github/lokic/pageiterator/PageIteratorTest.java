package com.github.lokic.pageiterator;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageIteratorTest {


    @Test
    public void pageNumTest() {
        List<List<String>> pageData = Lists.newArrayList(
                Lists.newArrayList("1", "2", "3"),
                Lists.newArrayList("4", "5", "6"),
                Lists.newArrayList("7", "8", "9"),
                Lists.newArrayList("10", "11")
        );
        PageNumPageTask<String, String> task = new PageNumPageTask<String, String>() {
            @Override
            List<String> getNextPage(int pageNum, int pageSize, String ctx) {
                return pageData.get(pageNum - 1);
            }

            @Override
            public int getPageSize() {
                return 3;
            }
        };
        PageNumPageTask<String, String> spyTask = Mockito.spy(task);
        Assert.assertEquals(Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"),
                Streams.stream(PageIterator.iterator(spyTask, "")).collect(Collectors.toList()));
        // 11个数据，，每页3个数据，4次取完
        Mockito.verify(spyTask, Mockito.times(4)).getNextPage(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.anyString());
    }

    @Test
    public void pageNumTest2() {
        List<List<String>> pageData = Lists.newArrayList(
                Lists.newArrayList("1", "2", "3"),
                Lists.newArrayList("4", "5", "6"),
                Lists.newArrayList("7", "8", "9"),
                Lists.newArrayList("10", "11", "12"),
                Lists.newArrayList()
        );
        PageNumPageTask<String, String> task = new PageNumPageTask<String, String>() {
            @Override
            List<String> getNextPage(int pageNum, int pageSize, String ctx) {
                return pageData.get(pageNum - 1);
            }

            @Override
            public int getPageSize() {
                return 3;
            }
        };
        PageNumPageTask<String, String> spyTask = Mockito.spy(task);
        Assert.assertEquals(Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"),
                Streams.stream(PageIterator.iterator(spyTask, "")).collect(Collectors.toList()));
        // 12个数据，，每页3个数据，4次取完，但是第5次需要判断数据已经取完，所以getNextPage会执行5次
        Mockito.verify(spyTask, Mockito.times(5)).getNextPage(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.anyString());
    }

    @Test
    public void preLastTest() {
        List<Integer> li = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);

        PreLastPageTask<Integer, String> task = new PreLastPageTask<Integer, String>() {
            @Override
            public int getPageSize() {
                return 3;
            }

            @Override
            Integer computePreLast(List<Integer> data, String cxt) {
                return data.stream().max(Comparator.comparing(Function.identity())).orElse(null);
            }

            @Override
            List<Integer> getNextPage(Integer preLast, int pageSize, String ctx) {
                int startId = preLast == null ? 0 : preLast;
                return li.stream()
                        .filter(x -> x > startId)
                        .limit(pageSize)
                        .collect(Collectors.toList());
            }
        };
        PreLastPageTask<Integer, String> spyTask = Mockito.spy(task);
        Assert.assertEquals(li, Streams.stream(PageIterator.iterator(spyTask, "")).collect(Collectors.toList()));
        // 11个数据，，每页3个数据，4次取完
        Mockito.verify(spyTask, Mockito.times(4)).getNextPage(Mockito.any(), Mockito.any(Integer.class), Mockito.anyString());
    }

    @Test
    public void maxPageNumTest() {
        List<List<String>> pageData = Lists.newArrayList(
                Lists.newArrayList("1", "2", "3"),
                Lists.newArrayList("4", "5", "6"),
                Lists.newArrayList("7", "8", "9"),
                Lists.newArrayList("10", "11")
        );
        PageNumPageTask<String, String> task = new PageNumPageTask<String, String>() {
            @Override
            List<String> getNextPage(int pageNum, int pageSize, String ctx) {
                return pageData.get(pageNum - 1);
            }

            @Override
            public int getPageSize() {
                return 3;
            }

            @Override
            public int getMaxPageNum() {
                return 2;
            }
        };
        PageNumPageTask<String, String> spyTask = Mockito.spy(task);
        Assert.assertEquals(
                Lists.newArrayList("1", "2", "3", "4", "5", "6"),
                Streams.stream(PageIterator.iterator(spyTask, "")).collect(Collectors.toList()));
        // 11个数据，，每页3个数据，4次取完，但是最大页数为2，所以只执行了2次
        Mockito.verify(spyTask, Mockito.times(2)).getNextPage(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.anyString());
    }


    @Test
    public void limitTest() {
        List<List<String>> pageData = Lists.newArrayList(
                Lists.newArrayList("1", "2", "3"),
                Lists.newArrayList("4", "5", "6"),
                Lists.newArrayList("7", "8", "9"),
                Lists.newArrayList("10", "11")
        );
        PageNumPageTask<String, String> task = new PageNumPageTask<String, String>() {
            @Override
            List<String> getNextPage(int pageNum, int pageSize, String ctx) {
                return pageData.get(pageNum - 1);
            }

            @Override
            public int getPageSize() {
                return 3;
            }
        };
        PageNumPageTask<String, String> spyTask = Mockito.spy(task);
        List<String> re = Streams.stream(PageIterator.iterator(spyTask, ""))
                .limit(4)
                .collect(Collectors.toList());

        Assert.assertEquals(Lists.newArrayList("1", "2", "3", "4"), re);
        // 11个数据，，每页3个数据，4次取完，但是limit=4，只需要取4个数据，所以只执行了2次
        Mockito.verify(spyTask, Mockito.times(2)).getNextPage(Mockito.anyString());
        Mockito.verify(spyTask, Mockito.times(2)).getNextPage(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.anyString());

    }

}