package org.example.document;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.example.ESUtils;

import java.awt.event.HierarchyBoundsAdapter;
import java.io.IOException;

public class Bulk {
    private static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-bulk.html
    public static void main(String[] args) throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("posts").id("2").source(XContentType.JSON, "field", "foo"));
        request.add(new IndexRequest("posts").id("3").source(XContentType.JSON, "field", "bar"));
        request.add(new IndexRequest("posts").id("4").source(XContentType.JSON, "field", "baz"));

        BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);

        client.close();
    }

}
