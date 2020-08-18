package org.example;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-supported-apis.html
 */

public class App 
{
    public static void main( String[] args ) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("10.10.10.246", 9200)));

        client.close();
    }
}
