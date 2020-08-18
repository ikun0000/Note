package org.example.document;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.example.ESUtils;

import java.io.IOException;

public class Delete {
    private static RestHighLevelClient client = ESUtils.getRestHighLevelClient();

    public static void main(String[] args) throws IOException {
        DeleteRequest request = new DeleteRequest("posts", "1");

        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            System.out.println("document not found");
        } else if (response.getResult() == DocWriteResponse.Result.DELETED) {
            System.out.println("Deleted");
        }

        client.close();
    }
}
