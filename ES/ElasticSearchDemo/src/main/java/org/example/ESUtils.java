package org.example;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ESUtils {
    private static volatile RestHighLevelClient restHighLevelClient;

    private ESUtils() {
    }

    public static RestHighLevelClient getRestHighLevelClient() {
        if (restHighLevelClient == null) {
            synchronized (ESUtils.class) {
                if (restHighLevelClient == null) {
                    restHighLevelClient = new RestHighLevelClient(
                            RestClient.builder(
                               new HttpHost("10.10.10.246", 9200)
                            ));
                }
            }
        }
        return restHighLevelClient;
    }

}
