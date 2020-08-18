package org.example.document;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.example.ESUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Index {
    private static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    public static IndexRequest sourceString() {
        IndexRequest request = new IndexRequest("posts")    // 索引
                .id("1");       // ID

        // 字符串方式的文档源，http body
        String jsonString = "{" +
                "\"user\": \"kimchy\"," +
                "\"postDate\": \"2013-01-30\"," +
                "\"message\": \"trying out Elasticsearch\"" +
                "}";

        request.source(jsonString, XContentType.JSON);

        return request;
    }

    public static IndexRequest sourceMap() {
        IndexRequest request = new IndexRequest("posts")
                .id("1");

        // Map方式提供的文档源，只会会转成JSON格式
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");

        request.source(jsonMap);

        return request;
    }

    public static IndexRequest sourceXContentBuilder() throws IOException {
        // XContentFactory构造的文档源
        XContentBuilder builder = XContentFactory.jsonBuilder();
        /**
         * 基础数据类型和包装类型都可以使用 builder.field() 装入
         * 日期类型使用 builder.timeField()
         * null值使用 builder.nullField()
         * InputStream使用 builder.rawField()
         */
        builder.startObject()
                .field("user", "kimchy")
                .timeField("postDate", new Date())
                .field("message", "trying out Elasticsearch")
                .endObject();
        IndexRequest request = new IndexRequest("posts")
                .id("1").source(builder);

        return request;
    }

    public static void syncRequest(IndexRequest request) throws IOException {
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        System.out.println("Status: " + response.status());
        System.out.println("ID: " + response.getId());
        System.out.println("Index: " + response.getIndex());
        System.out.println("Result: " + response.getResult());
    }

    public static void asyncRequest(IndexRequest request) {
        client.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println("Status: " + indexResponse.status());
                System.out.println("ID: " + indexResponse.getId());
                System.out.println("Index: " + indexResponse.getIndex());
                System.out.println("Result: " + indexResponse.getResult());
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
    public static void main(String[] args) throws IOException, InterruptedException {

        syncRequest(sourceXContentBuilder());

        Thread.sleep(2000);
        client.close();
    }

}
