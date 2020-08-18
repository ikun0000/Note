package org.example.document;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.example.ESUtils;

import java.io.IOException;

public class Exists {
    private static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    public static void main(String[] args) throws IOException {
        GetRequest request = new GetRequest("posts", "1");
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");

        System.out.println(client.exists(request, RequestOptions.DEFAULT));
        request.id("2");
        System.out.println(client.exists(request, RequestOptions.DEFAULT));

        client.close();
    }

}
