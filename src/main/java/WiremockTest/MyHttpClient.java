package WiremockTest;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

public class MyHttpClient {

    public static String getResponseFromWebService() throws IOException {
        //
        String hostname = "sapbp.dcdc.nl";

        int port = 8080;
        String scheme = "http";

        // make request
        File file = new File("src/test/resources/sapBpTestRequest.req.xml");
        String xmlRequest = FileUtils.readFileToString(file, "UTF-8");

        // proxy config - normally an environment setting
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpHost target = new HttpHost(hostname, port, scheme);
        HttpHost proxy = new HttpHost("127.0.0.1", port, scheme);
        RequestConfig proxyConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        // http request
        HttpPost httpPost = new HttpPost("/HandleBusinessPartner");
        // set proxy configuration
        httpPost.setConfig(proxyConfig);
        StringEntity entity = new StringEntity(xmlRequest);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/xml");
        httpPost.setHeader("Content-type", "text/xml");

        // execute request and catch response
        CloseableHttpResponse response = null;
        response = httpclient.execute(target, httpPost);

        ResponseHandler<String> handler = new BasicResponseHandler();
        String rsp = handler.handleResponse(response);
        response.close();
        return rsp;
    }
}
