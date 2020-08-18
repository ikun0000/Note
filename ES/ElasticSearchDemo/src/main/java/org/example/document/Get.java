package org.example.document;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.example.ESUtils;

import java.io.IOException;
import java.util.Map;

public class Get {
    public static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-get.html
    public static void main(String[] args) throws IOException {
        GetRequest request = new GetRequest("posts")
                .id("1");

        GetResponse response = client.get(request, RequestOptions.DEFAULT);

        String index = response.getIndex();
        String id = response.getId();
        if (response.isExists()) {
            long version = response.getVersion();
            String sourceAsString = response.getSourceAsString();
            Map<String, Object> sourceAsObject = response.getSourceAsMap();
            byte[] sourceAsBytes = response.getSourceAsBytes();

            sourceAsObject.forEach((key, value) -> {
                System.out.println("key: " + key + " value: " + value);
            });

        } else {
            System.out.println("request failure");
        }
        client.close();
    }

}
