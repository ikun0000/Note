package org.example.document;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.example.ESUtils;

import java.io.IOException;
import java.util.Date;

public class Update {
    private static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-update.html
    public static void main(String[] args) throws IOException {
        UpdateRequest request = new UpdateRequest("posts", "1");

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject()
                .timeField("updated", new Date())
                .field("reason", "daily update")
                .endObject();
        request.doc(builder);

        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

        if (response.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("CREATED");
        } else if (response.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("UPDATED");
        }

        client.close();
    }
}
