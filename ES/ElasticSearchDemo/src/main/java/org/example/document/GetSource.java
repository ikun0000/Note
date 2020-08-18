package org.example.document;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.example.ESUtils;

import java.util.Map;

public class GetSource {
    private static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-get-source.html
    public static void main(String[] args) {
        GetSourceRequest request = new GetSourceRequest("posts", "1");

        client.getSourceAsync(request, RequestOptions.DEFAULT, new ActionListener<GetSourceResponse>() {
            @Override
            public void onResponse(GetSourceResponse getSourceResponse) {
                Map<String, Object> source = getSourceResponse.getSource();
                source.forEach((key, value) -> {
                    System.out.println("key: " + key + " value: " + value);
                });
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
