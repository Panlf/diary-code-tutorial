package com.plf.diary.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;

public class ElasticSearchClient {
    public static void main(String[] args) throws IOException {
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient();


        //创建ES客户端
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200))
        );

        //创建索引
        //elasticSearchClient.createIndex(restHighLevelClient);

        //查询索引
        //elasticSearchClient.searchIndex(restHighLevelClient);

        //删除索引
        //elasticSearchClient.deleteIndex(restHighLevelClient);

        //插入数据
        //elasticSearchClient.insertData(restHighLevelClient);

        //更新数据
        //elasticSearchClient.updateData(restHighLevelClient);

        //查询数据
        //elasticSearchClient.searchData(restHighLevelClient);

        //批量插入
        //elasticSearchClient.insertDataBatch(restHighLevelClient);

        //全量查询
        //elasticSearchClient.matchAllQuery(restHighLevelClient);

        //条件查询
        //elasticSearchClient.termQuery(restHighLevelClient);

        //分页查询
        // elasticSearchClient.pageQuery(restHighLevelClient);

        //组合查询
        //elasticSearchClient.composeQuery(restHighLevelClient);

        //范围查询
        //elasticSearchClient.rangeQuery(restHighLevelClient);

        //模糊查询
        //elasticSearchClient.dimQuery(restHighLevelClient);

        //高亮查询
        //elasticSearchClient.highLightQuery(restHighLevelClient);

        //分组查询
        elasticSearchClient.groupQuery(restHighLevelClient);
        //关闭ES客户端
        restHighLevelClient.close();
    }


    public void groupQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");
        searchSourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response.getTook());

       Terms terms = response.getAggregations().get("ageGroup");
       terms.getBuckets().stream().forEach(x-> System.out.println(x.getKeyAsString()));
    }


    public void aggQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        AggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");
        searchSourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response.getTook());

        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getSourceAsString()));
    }

    public void highLightQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name.keyword","Blue");

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("name.keyword");

        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.query(termQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response.getTook());

        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getHighlightFields()));
    }

    public void dimQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.fuzzyQuery("name.keyword","An").fuzziness(Fuzziness.ONE));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getSourceAsString()));
    }

    public void rangeQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //多条件查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");

        rangeQueryBuilder.gte(30);
        rangeQueryBuilder.lte(40);

        searchSourceBuilder.query(rangeQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getSourceAsString()));
    }

    public void composeQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //多条件查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name","Ana"));
        boolQueryBuilder.must(QueryBuilders.matchQuery("age",25));

        searchSourceBuilder.query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getSourceAsString()));
    }

    public void pageQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());

        // (当前页码-1)*每页显示数据条数
        searchSourceBuilder.from(2);
        searchSourceBuilder.size(2);

        //排序
        searchSourceBuilder.sort("name.keyword", SortOrder.DESC);

        //过滤字段
        //需要排除的字段
        String[] excludes = {};
        //需要查询的字段
        String[] includes = {"name"};
        searchSourceBuilder.fetchSource(includes,excludes);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getSourceAsString()));

    }

    public void termQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");

        /**
         * term做精确查询可以用它来处理数字，布尔值，日期以及文本。查询数字时问题不大，但是当查询字符串时会有问题。
         * term查询的含义是termQuery会去倒排索引中寻找确切的term,但是它并不知道分词器的存在。
         * term表示查询字段里含有某个关键词的文档，terms表示查询字段里含有多个关键词的文档。
         * 也就是说直接对字段进行term本质上还是模糊查询，只不过不会对搜索的输入字符串进行分词处理罢了。
         * 如果想通过term查到数据，那么term查询的字段在索引库中就必须有与term查询条件相同的索引词，否则无法查询到结果。
         */
        //将字段的type设置为keyword
        //QueryBuilders.termQuery("name","Blue") 查不出来数据
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("name.keyword","Blue")));

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getSourceAsString()));

    }

    public void matchAllQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");


        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        Arrays.stream(hits.getHits()).forEach(x-> System.out.println(x.getSourceAsString()));

    }

    public void deleteDataBatch(RestHighLevelClient restHighLevelClient) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest().index("user").id("1001"));
        bulkRequest.add(new DeleteRequest().index("user").id("1002"));
        bulkRequest.add(new DeleteRequest().index("user").id("1003"));
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
    }

    public void insertDataBatch(RestHighLevelClient restHighLevelClient) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON,"name","Blue"));
        bulkRequest.add(new IndexRequest().index("user").id("1002").source(XContentType.JSON,"name","Davis"));
        bulkRequest.add(new IndexRequest().index("user").id("1003").source(XContentType.JSON,"name","Elfin"));
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
    }

    public void deleteData(RestHighLevelClient restHighLevelClient) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("user").id("1000");
        DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    public void searchData(RestHighLevelClient restHighLevelClient) throws IOException {
        GetRequest getRequest = new GetRequest();
        getRequest.index("user").id("1000");
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }

    public void updateData(RestHighLevelClient restHighLevelClient) throws IOException {
        UpdateRequest updateIndex = new UpdateRequest();
        updateIndex.index("user").id("1000");

        updateIndex.doc(XContentType.JSON,"sex","女");

        UpdateResponse response = restHighLevelClient.update(updateIndex, RequestOptions.DEFAULT);

        System.out.println(response.getResult());
    }

    public void insertData(RestHighLevelClient restHighLevelClient) throws IOException {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("user").id("1000");
        User user = new User();
        user.setName("Ana");
        user.setSex("男");
        user.setAge(25);

        //向ES存入数据，必须转换为JSON格式
        indexRequest.source(JSON.toJSONString(user),XContentType.JSON);

        IndexResponse response = restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    public void deleteIndex(RestHighLevelClient restHighLevelClient) throws IOException{
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("user");
        AcknowledgedResponse response = restHighLevelClient
                .indices()
                .delete(deleteIndexRequest,RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    public void searchIndex(RestHighLevelClient restHighLevelClient) throws IOException {
        GetIndexRequest request = new GetIndexRequest("user");
        GetIndexResponse response = restHighLevelClient.indices().get(request,RequestOptions.DEFAULT);

        System.out.println(response.getAliases());
        System.out.println(response.getDataStreams());
        System.out.println(response.getMappings());
    }


    public void createIndex(RestHighLevelClient restHighLevelClient) throws IOException {
        //创建索引
        CreateIndexRequest request =  new CreateIndexRequest("user");
        CreateIndexResponse createIndexResponse = restHighLevelClient
                .indices()
                .create(request, RequestOptions.DEFAULT);
        //响应状态
        boolean acknowledged = createIndexResponse.isAcknowledged();


        System.out.println("索引操作 : "+acknowledged);
    }
}
