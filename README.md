# page-iterator（分页迭代器）

## 简介

分页迭代器，更加优雅得处理分页迭代。

## 需求场景

有时候由于原始数据量比较大的时候，为了避免慢查询，我们常常会使用分页去取数据，在分页取数据的过程中，我们往往会写一个循环，然后在里面取分页数据进行处理。期间我们会关注分页怎么取数据，分页什么时候结束，取完数据之后怎么处理等问题。

本工具的目的就是做到 **数据提供方和数据处理方的职责分离**，数据提供方专注于分页行为本身应该关心的分页怎么取数据、分页什么时候结束等逻辑，而数据处理方只需要关心怎么处理数据。

## 开始使用

- 添加依赖

  ```xml
  <dependency>
      <groupId>com.github.lokic</groupId>
      <artifactId>page-iterator</artifactId>
      <version>${lastest.version}</version>
  </dependency>
  ```
  > 最新版本[查询](https://search.maven.org/search?q=a:page-iterator)
- 创建PageTask，现支持如下2种PageTask：

  - PageNumPageTask：基于页数取数据。传统的分页取法，页数从1开始
  - PreLastPageTask：基于上次取的最后一条数据取数据。有时需要基于上次取的最后一条数据取数据，比如取上一次的最后一个主键id

  ```java
  PageNumPageTask<String, String> task = new PageNumPageTask<String, String>() {
      @Override
      protected List<String> getNextPage(int pageNum, int pageSize, String ctx) {
          // 基于pageNum，获取对应页数的数据
          ...
      }
  
      @Override
      public int getPageSize() {
           // 每页的数据量
           return 3;
      }
  };
  
  
  PreLastPageTask<Integer, String> task = new PreLastPageTask<Integer, String>() {
      @Override
      protected Integer computePreLast(List<Integer> data, String cxt) {
           // 基于获取的本次获取的数据，计算最后一条数据
           ... 
      }
      
      @Override
      protected List<Integer> getNextPage(Integer preLast, int pageSize, String ctx) {
           // 基于preLast，获取对应页数的数据
          ...
      }
      
      @Override
      public int getPageSize() {
           return 3;
      }
  };
  ```

- 通过PageTask生成Iterator，然后就可以通过Iterator进行数据的遍历了。之后我们就只需要专注于获取数据之后的处理即可

  ```java
  Iterator<Integer> pageIterator = PageIterator.iterator(task, context);
  ```







