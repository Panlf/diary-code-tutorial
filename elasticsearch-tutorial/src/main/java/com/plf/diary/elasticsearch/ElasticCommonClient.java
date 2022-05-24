package com.plf.diary.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

public class ElasticCommonClient {

    RestHighLevelClient restHighLevelClient = null;

    /**
     * ES集群
     */
    public void createClient() {
        restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("....", 9200),
                        new HttpHost("....", 9200),
                        new HttpHost("....", 9200))
        );
    }

    public void countIndex(String index) {
        try {
            createClient();
            CountRequest countRequest = new CountRequest(index);
            CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            System.out.println(countResponse.getCount());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeClient();
        }
    }


    public List<String> scrollData(String index) {
        try {
            createClient();
            String scrollId = "";
            List<String> result = new ArrayList<>();
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);
            searchRequest.scroll("1m");
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = response.getHits();
            for (int i = 0; i < hits.getHits().length; i++) {
                result.add(hits.getHits()[i].getSourceAsString());
            }
            //记录滚动ID
            scrollId = response.getScrollId();

            while (true) {
                if (scrollId == null || scrollId.equals("")) {
                    break;
                }
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId).scroll(TimeValue.timeValueMinutes(1));

                response = restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);
                if (response != null && response.getHits().getHits().length > 0) {
                    for (SearchHit hit : response.getHits().getHits()) {
                        result.add(hit.getSourceAsString());
                    }
                    scrollId = response.getScrollId();
                } else {
                    break;
                }
            }
            //清楚Scroll
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            return  result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeClient();
        }
        return null;
    }

    public void searchDataPage(String index) {
        try {
            createClient();
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());

            // (当前页码-1)*每页显示数据条数
            searchSourceBuilder.from(1);
            searchSourceBuilder.size(300);
            searchRequest.source(searchSourceBuilder);

            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();

            Arrays.stream(hits.getHits()).forEach(x -> System.out.println(x.getSourceAsString()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeClient();
        }
    }

    public void getFiledByIndex(String index) {
        try {
            createClient();
            GetMappingsRequest getMappings = new GetMappingsRequest().indices(index);
            //调用获取
            GetMappingsResponse getMappingResponse = restHighLevelClient.indices().getMapping(getMappings, RequestOptions.DEFAULT);
            //处理数据
            Map<String, MappingMetadata> allMappings = getMappingResponse.mappings();
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Map.Entry<String, MappingMetadata> indexValue : allMappings.entrySet()) {
                Map<String, Object> mapping = indexValue.getValue().sourceAsMap();
                Iterator<Map.Entry<String, Object>> entries = mapping.entrySet().iterator();
                entries.forEachRemaining(stringObjectEntry -> {
                    if (stringObjectEntry.getKey().equals("properties")) {
                        Map<String, Object> value = (Map<String, Object>) stringObjectEntry.getValue();
                        for (Map.Entry<String, Object> ObjectEntry : value.entrySet()) {
                            Map<String, Object> map = new HashMap<>();
                            String key = ObjectEntry.getKey();
                            Map<String, Object> value1 = (Map<String, Object>) ObjectEntry.getValue();
                            map.put(key, value1.get("type"));
                            mapList.add(map);
                        }
                    }
                });
            }
            System.out.println(mapList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClient();
        }
    }


    /**
     * 获取了全部Index
     * @return
     */
    public Set<String> getAllIndices() {
        try {
            createClient();
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
            return map.keySet();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClient();
        }
        return new HashSet<>();
    }


    public void closeClient() {
        if (restHighLevelClient != null) {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
