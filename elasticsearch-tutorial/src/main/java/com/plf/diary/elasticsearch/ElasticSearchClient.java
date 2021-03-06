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


        //??????ES?????????
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200))
        );

        //????????????
        //elasticSearchClient.createIndex(restHighLevelClient);

        //????????????
        //elasticSearchClient.searchIndex(restHighLevelClient);

        //????????????
        //elasticSearchClient.deleteIndex(restHighLevelClient);

        //????????????
        //elasticSearchClient.insertData(restHighLevelClient);

        //????????????
        //elasticSearchClient.updateData(restHighLevelClient);

        //????????????
        //elasticSearchClient.searchData(restHighLevelClient);

        //????????????
        //elasticSearchClient.insertDataBatch(restHighLevelClient);

        //????????????
        //elasticSearchClient.matchAllQuery(restHighLevelClient);

        //????????????
        //elasticSearchClient.termQuery(restHighLevelClient);

        //????????????
        // elasticSearchClient.pageQuery(restHighLevelClient);

        //????????????
        //elasticSearchClient.composeQuery(restHighLevelClient);

        //????????????
        //elasticSearchClient.rangeQuery(restHighLevelClient);

        //????????????
        //elasticSearchClient.dimQuery(restHighLevelClient);

        //????????????
        //elasticSearchClient.highLightQuery(restHighLevelClient);

        //????????????
        elasticSearchClient.groupQuery(restHighLevelClient);
        //??????ES?????????
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

        //???????????????
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

        //???????????????
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

        // (????????????-1)*????????????????????????
        searchSourceBuilder.from(2);
        searchSourceBuilder.size(2);

        //??????
        searchSourceBuilder.sort("name.keyword", SortOrder.DESC);

        //????????????
        //?????????????????????
        String[] excludes = {};
        //?????????????????????
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
         * term??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * term??????????????????termQuery????????????????????????????????????term,??????????????????????????????????????????
         * term??????????????????????????????????????????????????????terms??????????????????????????????????????????????????????
         * ?????????????????????????????????term???????????????????????????????????????????????????????????????????????????????????????????????????
         * ???????????????term?????????????????????term?????????????????????????????????????????????term???????????????????????????????????????????????????????????????
         */
        //????????????type?????????keyword
        //QueryBuilders.termQuery("name","Blue") ??????????????????
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

        updateIndex.doc(XContentType.JSON,"sex","???");

        UpdateResponse response = restHighLevelClient.update(updateIndex, RequestOptions.DEFAULT);

        System.out.println(response.getResult());
    }

    public void insertData(RestHighLevelClient restHighLevelClient) throws IOException {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("user").id("1000");
        User user = new User();
        user.setName("Ana");
        user.setSex("???");
        user.setAge(25);

        //???ES??????????????????????????????JSON??????
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
        //????????????
        CreateIndexRequest request =  new CreateIndexRequest("user");
        CreateIndexResponse createIndexResponse = restHighLevelClient
                .indices()
                .create(request, RequestOptions.DEFAULT);
        //????????????
        boolean acknowledged = createIndexResponse.isAcknowledged();


        System.out.println("???????????? : "+acknowledged);
    }
}
