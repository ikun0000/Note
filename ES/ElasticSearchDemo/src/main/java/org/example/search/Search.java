package org.example.search;

import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.example.ESUtils;

import java.io.IOException;
import java.util.Map;

public class Search {
    private static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search.html
    public static void main(String[] args) throws IOException {
        SearchRequest request = new SearchRequest("posts");

        /**
         * SearchSourceBuilder 就相当于HTTP body的内容
         * {
         *      "query": {
         *          "match_all": {}
         *      }
         * }
         */
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();

        for (SearchHit hit : searchHits) {
            Map<String, Object> map = hit.getSourceAsMap();
            map.forEach((key, value) -> {
                System.out.println("key: " + key + " value: " + value);
            });
        }

        client.close();
    }

}
