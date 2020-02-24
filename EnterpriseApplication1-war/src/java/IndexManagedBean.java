/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

;

@Named(value = "indexManagedBean")
@ViewScoped
public class IndexManagedBean implements Serializable {

    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {

    }

    public void upload() throws Exception {

        String payload = "{ "
                + "   \"tasks\":{ "
                + "      \"import-1\":{ "
                + "         \"operation\":\"import/upload\""
                + "      }"
                + "   }"
                + "}";
        //  StringEntity entity = new StringEntity(payload,
        //  ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost("https://api.cloudconvert.com/v2/import/upload");

        request.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk0OGViZTBiZGU1ZTdhMDc4M2VhZjVkZjYxZWRlNTUxOTNiYWU3MjViM2Y0ODUzZGNjMjc5Mjg3ODlmYjU3NDg2ZWUwMjg0NzE0ZjZlY2UxIn0.eyJhdWQiOiIxIiwianRpIjoiOTQ4ZWJlMGJkZTVlN2EwNzgzZWFmNWRmNjFlZGU1NTE5M2JhZTcyNWIzZjQ4NTNkY2MyNzkyODc4OWZiNTc0ODZlZTAyODQ3MTRmNmVjZTEiLCJpYXQiOjE1ODIyOTg1NDIsIm5iZiI6MTU4MjI5ODU0MiwiZXhwIjo0NzM3OTcyMTQyLCJzdWIiOiI0MDQ2MTIzNyIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.YB2HdA00K9JTZB1X78tdFHyjxCPtpKYhu1OcNGrHw2jqAlA2EYRECXIGPzgsjpGrhjZT3VEpBU9pnk8FVyIf07pnxgsQ9ZgfAE7Zgd25r5J9FLwrHYB8GINWX8IoZE39-W_wkBEFSmuuE1niF22Q7aFStHesMQ1svjPu6ftRWHHGxv1sBNn1XvB62oxXRQA3ijznKDjKr8tSuexZ6exLSxzaDd_awoCTma_3v1hpSE7k3EbE5IOGjTZjGTKRrnwyKXjiMqf4kddwNKMTqq6RoXPZ5o0LIlNwxlp9wD89q8bO7qnECNPx8QLq-RngpCWWqD6gJSV8YrjeHgx3cM0UcdEE9nKFMT-DCVtQpiHjNh2WoUeML1QIF5asTrE4FiAk7beHTMwOSymym-8plG58M3I74hBmEl_93Or0xrCVA9nsVLcJhxlWCG9_R5T0mQRsmOt9kIrrMNRiFoQ8uZfBV9EPyVPpkhnBk_2RccVYXMgAo-dPQH3kmsH0dYn8fpPi1shJwfIkcQjvpJKuGErWAQeBmjxr0c9mgk9JNsVJ2X_AyZjAEX3SqVnarK42o7klVCylQqzzm6tFan2P_4jy2flmuOzLXnv_dcfIL_nztaxx_FAE7wHkUKR-IYqm27eor0gWFUSNZW09LfgPOZ-eVP7_P4lNKgp1tIigaCquK1M");
        // request.setEntity(entity);

        //  HttpResponse response = httpClient.execute(request);
        //  System.out.println(response.getStatusLine().getStatusCode());
        System.out.println("Executing request " + request.getRequestLine());

        // Create a custom response handler
        ResponseHandler<String> responseHandler = response2 -> {
            int status = response2.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity2 = response2.getEntity();
                return entity2 != null ? EntityUtils.toString(entity2) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpClient.execute(request, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        JSONObject gss = new JSONObject(responseBody);

        JSONObject form = gss.getJSONObject("data").getJSONObject("result").getJSONObject("form");
        JSONObject para = gss.getJSONObject("data").getJSONObject("result").getJSONObject("form").getJSONObject("parameters");
        String redirect = para.getString("redirect");
        String signature = para.getString("signature");
        int expireString = para.getInt("expires");
        int maxFileSize = para.getInt("max_file_size");
        int maxFileCount = para.getInt("max_file_count");
        String uploadURL = form.getString("url");
        String jobId = gss.getJSONObject("data").getString("id");
        System.out.println("url " + uploadURL);
        System.out.println("expire " + expireString);
        System.out.println("maxFileSize " + maxFileSize);
        System.out.println("maxFileCount " + maxFileCount);
        System.out.println("redirect " + redirect);
        System.out.println("signature " + signature);

        try (CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build()) {
            System.out.println("un");

            String boundary = Long.toHexString(System.currentTimeMillis());

            File file = new File("/Users/mingxuan/Desktop/[RSL CORP] USS Express Pass_28112019.pdf");

            HttpEntity entity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setBoundary(boundary)
                    .addTextBody("expires", Integer.toString(expireString))
                    .addTextBody("max_file_count", Integer.toString(maxFileCount))
                    .addTextBody("max_file_size", "10000000000")
                    .addTextBody("redirect", redirect)
                    .addTextBody("signature", signature)
                    .addBinaryBody("file", new FileInputStream(file), ContentType.DEFAULT_BINARY, file.getName())
                    //  .addTextBody("tasks[import-1][operation]", "import/upload")
                    //  .addBinaryBody("file", new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM, file.getName()) 
                    //   .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())

                    //   .addTextBody("tasks[task-1][operation]", "convert")
                    // .addTextBody("json", jsonToSend.toString())
                    //   .addTextBody("tasks", "{\"import-1\": {\"operation\": \"import/upload\"}}",ContentType.DEFAULT_BINARY)
                    .build();

            HttpUriRequest newRequest = RequestBuilder
                    .post(uploadURL)
                    .setEntity(entity)
                    //   .setHeader("Connection", "Keep-Alive")
                    // .setHeader("Cache-Control", "no-cache")
                    .setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjgxNDkxNWNmZDFmY2Y4NjgyZTYzYTcxMjgzMmRlZjc5Mjc4NjJmODRmYTdiN2QwNDYxOTBjNDRkYzk1NmJmZTY4ZDVkNTZlY2FkMmMyMjVkIn0.eyJhdWQiOiIxIiwianRpIjoiODE0OTE1Y2ZkMWZjZjg2ODJlNjNhNzEyODMyZGVmNzkyNzg2MmY4NGZhN2I3ZDA0NjE5MGM0NGRjOTU2YmZlNjhkNWQ1NmVjYWQyYzIyNWQiLCJpYXQiOjE1ODIyNzIyMjQsIm5iZiI6MTU4MjI3MjIyNCwiZXhwIjo0NzM3OTQ1ODI0LCJzdWIiOiIzOTgyODc4OSIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay53cml0ZSIsInRhc2sucmVhZCIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.ov2FqKmBhQPA3poQoavkGjiMdUimXq1eh9OTY7zzFW0JBmpSlHNnwjjA7NwbSKdUqhhevXC3opbMh-HaBY8nlkJfhyO17Bkj0omMKudF68LJ8rcLjuz0w1GHzFuIxmq5Yb9ozPcsvr7p1oK2x4F_-qF9sdmlkBkQ1Ryrh3zZUx4Y_vgQB44ScTwRdcc0wQYn_1aQTOWHNwrqI7YFQhil-qcWqK8i7UozPQtoguWoEupvyGCvbMDtIIne1GbSA3BqE15BOlRLMygLwnlLiEXxNmlvhYmgcaEoNQdDl1E0UkzdAvCdEE_5Gl2fp1opZFGf-qlBZ8FKxobXc7mil1VLHvU74r6qId4JtCl6Vd4qHGi2JpD5mC7LN9yu3LJ5m1m_-3LG4g1T-nsIIFsI32o0hEJm2EJEA6AAj0LDqPR4QXSzI7utS1W_KCnNg1s_EuocW4NNSLQDcBGdatvQz1oSQ6GEIejMmDIbhpgsFxI_1lSZNxbz1u6ekLCffBqyHMpOokP7WpeARCoYOPjFkXaixEGrXq0UyhHeeBtZ7FIp5DyOzkA7XQ9fcbWX2QpHKUtOgtBS6kOVJzG18CSnFsb-jxtcQjudbPAdjN8Dv2DtE7kzp9vI_u71IOIIqy11Np3qmAaNQOK9jUocFJjbAEsMypqyTlnT41Nba91hkNdOQQM")
                    .setHeader("Content-type", "application/json")
                    .setHeader("Content-Type", "multipart/form-data" + ";boundary=" + boundary)
                    // .setHeader("User-Agent", "CodeJava Agent")

                    .build();

            System.out.println("Executing request " + newRequest.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> newResponseHandler = newResponse -> {
                int status = newResponse.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity2 = newResponse.getEntity();
                    return entity2 != null ? EntityUtils.toString(entity2) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String newResponseBody = httpclient.execute(newRequest, newResponseHandler);
            System.out.println("----------------------------------------");
            System.out.println(newResponseBody);

            HttpClient httpClient20 = HttpClientBuilder.create().build();

            HttpPost request20 = new HttpPost("https://api.cloudconvert.com/v2/optimize");
            JSONObject json = new JSONObject();
            json.put("input", jobId);
            json.put("input_format", "pdf");
            json.put("profile", "max");
            StringEntity params = new StringEntity(json.toString());
            request20.setEntity(params);
            request20.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk0OGViZTBiZGU1ZTdhMDc4M2VhZjVkZjYxZWRlNTUxOTNiYWU3MjViM2Y0ODUzZGNjMjc5Mjg3ODlmYjU3NDg2ZWUwMjg0NzE0ZjZlY2UxIn0.eyJhdWQiOiIxIiwianRpIjoiOTQ4ZWJlMGJkZTVlN2EwNzgzZWFmNWRmNjFlZGU1NTE5M2JhZTcyNWIzZjQ4NTNkY2MyNzkyODc4OWZiNTc0ODZlZTAyODQ3MTRmNmVjZTEiLCJpYXQiOjE1ODIyOTg1NDIsIm5iZiI6MTU4MjI5ODU0MiwiZXhwIjo0NzM3OTcyMTQyLCJzdWIiOiI0MDQ2MTIzNyIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.YB2HdA00K9JTZB1X78tdFHyjxCPtpKYhu1OcNGrHw2jqAlA2EYRECXIGPzgsjpGrhjZT3VEpBU9pnk8FVyIf07pnxgsQ9ZgfAE7Zgd25r5J9FLwrHYB8GINWX8IoZE39-W_wkBEFSmuuE1niF22Q7aFStHesMQ1svjPu6ftRWHHGxv1sBNn1XvB62oxXRQA3ijznKDjKr8tSuexZ6exLSxzaDd_awoCTma_3v1hpSE7k3EbE5IOGjTZjGTKRrnwyKXjiMqf4kddwNKMTqq6RoXPZ5o0LIlNwxlp9wD89q8bO7qnECNPx8QLq-RngpCWWqD6gJSV8YrjeHgx3cM0UcdEE9nKFMT-DCVtQpiHjNh2WoUeML1QIF5asTrE4FiAk7beHTMwOSymym-8plG58M3I74hBmEl_93Or0xrCVA9nsVLcJhxlWCG9_R5T0mQRsmOt9kIrrMNRiFoQ8uZfBV9EPyVPpkhnBk_2RccVYXMgAo-dPQH3kmsH0dYn8fpPi1shJwfIkcQjvpJKuGErWAQeBmjxr0c9mgk9JNsVJ2X_AyZjAEX3SqVnarK42o7klVCylQqzzm6tFan2P_4jy2flmuOzLXnv_dcfIL_nztaxx_FAE7wHkUKR-IYqm27eor0gWFUSNZW09LfgPOZ-eVP7_P4lNKgp1tIigaCquK1M");
            request20.setHeader("Content-type", "application/json");

            // request.setEntity(entity);
            //  HttpResponse response = httpClient.execute(request);
            //  System.out.println(response.getStatusLine().getStatusCode());
            System.out.println("Executing request " + request20.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler2 = response3 -> {
                int status = response3.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity2 = response3.getEntity();
                    return entity2 != null ? EntityUtils.toString(entity2) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody3 = httpClient20.execute(request20, responseHandler2);
            System.out.println("----------------------------------------");
            System.out.println(responseBody3);

            JSONObject vcxvcxvxc = new JSONObject(responseBody3);
            String compressId = vcxvcxvxc.getJSONObject("data").getString("id");

            HttpClient httpClient30 = HttpClientBuilder.create().build();

            HttpPost request30 = new HttpPost("https://api.cloudconvert.com/v2/export/url");
            JSONObject json11 = new JSONObject();
            json11.put("input", compressId);
            // json11.put("inline", true); 
            json11.put("archive_multiple_files", false);
            StringEntity params1 = new StringEntity(json11.toString());
            request30.setEntity(params1);
            request30.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk0OGViZTBiZGU1ZTdhMDc4M2VhZjVkZjYxZWRlNTUxOTNiYWU3MjViM2Y0ODUzZGNjMjc5Mjg3ODlmYjU3NDg2ZWUwMjg0NzE0ZjZlY2UxIn0.eyJhdWQiOiIxIiwianRpIjoiOTQ4ZWJlMGJkZTVlN2EwNzgzZWFmNWRmNjFlZGU1NTE5M2JhZTcyNWIzZjQ4NTNkY2MyNzkyODc4OWZiNTc0ODZlZTAyODQ3MTRmNmVjZTEiLCJpYXQiOjE1ODIyOTg1NDIsIm5iZiI6MTU4MjI5ODU0MiwiZXhwIjo0NzM3OTcyMTQyLCJzdWIiOiI0MDQ2MTIzNyIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.YB2HdA00K9JTZB1X78tdFHyjxCPtpKYhu1OcNGrHw2jqAlA2EYRECXIGPzgsjpGrhjZT3VEpBU9pnk8FVyIf07pnxgsQ9ZgfAE7Zgd25r5J9FLwrHYB8GINWX8IoZE39-W_wkBEFSmuuE1niF22Q7aFStHesMQ1svjPu6ftRWHHGxv1sBNn1XvB62oxXRQA3ijznKDjKr8tSuexZ6exLSxzaDd_awoCTma_3v1hpSE7k3EbE5IOGjTZjGTKRrnwyKXjiMqf4kddwNKMTqq6RoXPZ5o0LIlNwxlp9wD89q8bO7qnECNPx8QLq-RngpCWWqD6gJSV8YrjeHgx3cM0UcdEE9nKFMT-DCVtQpiHjNh2WoUeML1QIF5asTrE4FiAk7beHTMwOSymym-8plG58M3I74hBmEl_93Or0xrCVA9nsVLcJhxlWCG9_R5T0mQRsmOt9kIrrMNRiFoQ8uZfBV9EPyVPpkhnBk_2RccVYXMgAo-dPQH3kmsH0dYn8fpPi1shJwfIkcQjvpJKuGErWAQeBmjxr0c9mgk9JNsVJ2X_AyZjAEX3SqVnarK42o7klVCylQqzzm6tFan2P_4jy2flmuOzLXnv_dcfIL_nztaxx_FAE7wHkUKR-IYqm27eor0gWFUSNZW09LfgPOZ-eVP7_P4lNKgp1tIigaCquK1M");
            request30.setHeader("Content-type", "application/json");

            // request.setEntity(entity);
            //  HttpResponse response = httpClient.execute(request);
            //  System.out.println(response.getStatusLine().getStatusCode());
            System.out.println("Executing request " + request30.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler4 = response5 -> {
                int status = response5.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity2 = response5.getEntity();
                    return entity2 != null ? EntityUtils.toString(entity2) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody5 = httpClient30.execute(request30, responseHandler4);
            System.out.println("----------------------------------------");
            System.out.println(responseBody5);
            JSONObject dfgdfg = new JSONObject(responseBody5);
            String exportId = dfgdfg.getJSONObject("data").getString("id");
            //  JSONArray filesz= dfgdfg.getJSONObject("data").getJSONObject("result").getJSONArray("files");
            //   System.out.println(filesz);

            HttpClient httpClient500 = HttpClientBuilder.create().build();

            HttpGet request500 = new HttpGet("https://api.cloudconvert.com/v2/tasks/" + exportId + "/wait");

            request500.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk0OGViZTBiZGU1ZTdhMDc4M2VhZjVkZjYxZWRlNTUxOTNiYWU3MjViM2Y0ODUzZGNjMjc5Mjg3ODlmYjU3NDg2ZWUwMjg0NzE0ZjZlY2UxIn0.eyJhdWQiOiIxIiwianRpIjoiOTQ4ZWJlMGJkZTVlN2EwNzgzZWFmNWRmNjFlZGU1NTE5M2JhZTcyNWIzZjQ4NTNkY2MyNzkyODc4OWZiNTc0ODZlZTAyODQ3MTRmNmVjZTEiLCJpYXQiOjE1ODIyOTg1NDIsIm5iZiI6MTU4MjI5ODU0MiwiZXhwIjo0NzM3OTcyMTQyLCJzdWIiOiI0MDQ2MTIzNyIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.YB2HdA00K9JTZB1X78tdFHyjxCPtpKYhu1OcNGrHw2jqAlA2EYRECXIGPzgsjpGrhjZT3VEpBU9pnk8FVyIf07pnxgsQ9ZgfAE7Zgd25r5J9FLwrHYB8GINWX8IoZE39-W_wkBEFSmuuE1niF22Q7aFStHesMQ1svjPu6ftRWHHGxv1sBNn1XvB62oxXRQA3ijznKDjKr8tSuexZ6exLSxzaDd_awoCTma_3v1hpSE7k3EbE5IOGjTZjGTKRrnwyKXjiMqf4kddwNKMTqq6RoXPZ5o0LIlNwxlp9wD89q8bO7qnECNPx8QLq-RngpCWWqD6gJSV8YrjeHgx3cM0UcdEE9nKFMT-DCVtQpiHjNh2WoUeML1QIF5asTrE4FiAk7beHTMwOSymym-8plG58M3I74hBmEl_93Or0xrCVA9nsVLcJhxlWCG9_R5T0mQRsmOt9kIrrMNRiFoQ8uZfBV9EPyVPpkhnBk_2RccVYXMgAo-dPQH3kmsH0dYn8fpPi1shJwfIkcQjvpJKuGErWAQeBmjxr0c9mgk9JNsVJ2X_AyZjAEX3SqVnarK42o7klVCylQqzzm6tFan2P_4jy2flmuOzLXnv_dcfIL_nztaxx_FAE7wHkUKR-IYqm27eor0gWFUSNZW09LfgPOZ-eVP7_P4lNKgp1tIigaCquK1M");
            // request.setEntity(entity);

            //  HttpResponse response = httpClient.execute(request);
            //  System.out.println(response.getStatusLine().getStatusCode());
            System.out.println("Executing request " + request500.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler500 = response500 -> {
                int status = response500.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity2 = response500.getEntity();
                    return entity2 != null ? EntityUtils.toString(entity2) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody500 = httpClient500.execute(request500, responseHandler500);
            System.out.println("----------------------------------------");
            System.out.println(responseBody500);
            JSONObject bcvbcvbv = new JSONObject(responseBody500);
            JSONArray filesz = bcvbcvbv.getJSONObject("data").getJSONObject("result").getJSONArray("files");
            System.out.println(filesz);
            String downloadLink = filesz.getJSONObject(0).getString("url");
            System.out.println("downloadlink" + downloadLink);
            downloadFile(downloadLink,file);
            //  JSONObject gdfd=new JSONObject(newResponseBody);
            // System.out.println(gss.getJSONObject("data").getJSONArray("tasks"));

            /*      request = RequestBuilder
                    .get("https://api.cloudconvert.com/v2/jobs/"+ jobId + "/wait")
                 //   .setEntity(entity)
                 //   .setHeader("Connection", "Keep-Alive")
                   // .setHeader("Cache-Control", "no-cache")
                    .setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjllNDMwOGVjMDE1YjllNmJmMTk1NDExYTUxN2QxYmY5NDM1OGE4ZDdiZTNmZTNiYTA3MDk3MGQzZGIzYWViNWVkODQ1MTczODNjZTNkNjdiIn0.eyJhdWQiOiIxIiwianRpIjoiOWU0MzA4ZWMwMTViOWU2YmYxOTU0MTFhNTE3ZDFiZjk0MzU4YThkN2JlM2ZlM2JhMDcwOTcwZDNkYjNhZWI1ZWQ4NDUxNzM4M2NlM2Q2N2IiLCJpYXQiOjE1ODIyNjk1NDcsIm5iZiI6MTU4MjI2OTU0NywiZXhwIjo0NzM3OTQzMTQ3LCJzdWIiOiI0MDQ2MTIzNyIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.j8zxVC-yLy5aL0Aw_MrJYmqDTRr-XCeT76PwP_HZDql5ktklcFzRfqXUP5DLj5BkjZaQg7D_8YnJbRxYcUUP0Qb2F1LqOpiD31aJk4Q_jef6hxtWaDnuTGleFoHtN5XQYWCCliE3v2pbVXJUuaYDv4heVFcrlEgBM6QWtyzF1xwu8ORjL7OSPzL6cIiPjg74PnrAPpfaeVFthAXWB06xCP6yZOTGa32C1WNDvb3qoK8_6pbCs5qpkm-83HVNEs6TtUTQ0-I166fGLYJLXc7JHK-Cdxl2jXTCQaPA0nBIKjszbTpSgWDBnYvqXMJlOtfL26NlvTNjetd_0ryyrYOdgMR7MHbtpLHtIRMlyh2JF0RQcYdREoBdheFOvu4Sc3I6LP4RdWRhlZayUhSJNrAuwqF6jfzODSyVF7f2ZAzS-M1plkjQhBPsWfw3XxlT1IHlcmvCSBj0jRyU_NRquQCoHuaC7WhhEoWV0rYVY2nYGZZDCrjlv7b9w2zBaPEGnnnjGt2mAvOVeViC4kM6s2zwn9Ya1AoW89crqqHJSdl7SNz42TJJxjkQVQh8F46ohXf7EKXnHvAUrEaYIgZqpEz7_KIOqM1s1xvs9svLr4A7X9GPQ9vOGT04ofBA77WzaIyCpomi8BLAKDzwlIDEGUC8HZbkO_uxEgLNV5Sa9hQwu_E")
                  //    .setHeader("Content-type" ,"application/json" + ";boundary=" + boundary)
                  // .setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType() + ";boundary=" + boundary)
                   // .setHeader("User-Agent", "CodeJava Agent")
                    .build();

            System.out.println("Executing request " + request.getRequestLine());

            // Create a custom response handler
                responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity2 = response.getEntity();
                    return entity2 != null ? EntityUtils.toString(entity2) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
             responseBody = httpclient.execute(request, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
             
            // HttpUriRequest request = RequestBuilder
            //   .post("https://api.cloudconvert.com/v2/jobs")
            //   request.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjZhNmIyMWUyNmVkOWI3MjVlZWE0ZDVlMjVmOGQ0NzUyMTdkMDdmMmViODgwY2RlODYzYTQ5ODUzNTc0NmRhMjg5YjQ0MWJjYjhmODg2ODdiIn0.eyJhdWQiOiIxIiwianRpIjoiNmE2YjIxZTI2ZWQ5YjcyNWVlYTRkNWUyNWY4ZDQ3NTIxN2QwN2YyZWI4ODBjZGU4NjNhNDk4NTM1NzQ2ZGEyODliNDQxYmNiOGY4ODY4N2IiLCJpYXQiOjE1ODE5MDYwNjgsIm5iZiI6MTU4MTkwNjA2OCwiZXhwIjo0NzM3NTc5NjY4LCJzdWIiOiIzOTgxOTc0MCIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.Pi9JjKrcKb0CygaRl66NYwz3orfAftpElS9v1FPIEHehoRI7Cn5dBpepraQu17mXLkFHH3wBlJk12b16KL-tbbxidusfmZCyH37ZNKfV_5BWEzVE1RlrKiP5l8mGZvaYCkWUC-khAD4nDZfPOrNtMzkLgP47TPcflS1lWAGLm6lvCNf2OBM1n2-Z4JIcl6HgF-autKW4RoV9Svkexj8nQUY3JT23Hq7Dq5lxnwrzphxiLkYKYMPB81bwbL-4JJEF9TD9qcZ9ESRzQcyYczT3870K1rsAXD5vkDR2ov-ZXSbRNkw2Hj6PAYeE_tmFJINl98u1OPz5cEqMyRldBSogZiSWYhtCO2hQnqjWsCQFgM5iOFFbmw4u2wpn8D0a2vZmR6KbVHtDiNViDkdnBOZrsnIoB0lzg_BFNgOg0HW3JEH9y-nk133c8mbRrVhy3ImmuSXPj3mwWEl7WEI4yzw1Ri5PU-RNnJmJheb6ca-p5qpsj_anUPGZlnYZGuJau1U-KeLLnZoVbAFqWmfm__1D24iq8MVMeX7brWL7G0ZamoA-1AchZ6WUgXcYZ0qH4bdlSSiRs8xb47DBFzAyikdlTaMMXrpxmhmLB7hlpYe3abxhw8IvoOHHDswaeeofWR8fYWxnGP3aabJ24A0GLdJCz9kcb1GarT4Wq0cOeiDP9WQ")
            //  request.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType() + ";boundary=" + boundary);
            //   String response = httpClient.execute(httpPost);
            //   HttpEntity result = response.getEntity();
            //  System.out.println("Executing request " + httpPost.getRequestLine());
            //  String rsp =inputStreamToString(response.getEntity().getContent());
            //  System.out.println(rsp);
            //  HttpGet httpPost = new HttpGet("https://api.cloudconvert.com/v2/tasks/a12a69b0-db20-4767-94ac-887f38f885d8/wait");
            //  httpPost.setEntity(entity);
            //    httpPost.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjZhNmIyMWUyNmVkOWI3MjVlZWE0ZDVlMjVmOGQ0NzUyMTdkMDdmMmViODgwY2RlODYzYTQ5ODUzNTc0NmRhMjg5YjQ0MWJjYjhmODg2ODdiIn0.eyJhdWQiOiIxIiwianRpIjoiNmE2YjIxZTI2ZWQ5YjcyNWVlYTRkNWUyNWY4ZDQ3NTIxN2QwN2YyZWI4ODBjZGU4NjNhNDk4NTM1NzQ2ZGEyODliNDQxYmNiOGY4ODY4N2IiLCJpYXQiOjE1ODE5MDYwNjgsIm5iZiI6MTU4MTkwNjA2OCwiZXhwIjo0NzM3NTc5NjY4LCJzdWIiOiIzOTgxOTc0MCIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.Pi9JjKrcKb0CygaRl66NYwz3orfAftpElS9v1FPIEHehoRI7Cn5dBpepraQu17mXLkFHH3wBlJk12b16KL-tbbxidusfmZCyH37ZNKfV_5BWEzVE1RlrKiP5l8mGZvaYCkWUC-khAD4nDZfPOrNtMzkLgP47TPcflS1lWAGLm6lvCNf2OBM1n2-Z4JIcl6HgF-autKW4RoV9Svkexj8nQUY3JT23Hq7Dq5lxnwrzphxiLkYKYMPB81bwbL-4JJEF9TD9qcZ9ESRzQcyYczT3870K1rsAXD5vkDR2ov-ZXSbRNkw2Hj6PAYeE_tmFJINl98u1OPz5cEqMyRldBSogZiSWYhtCO2hQnqjWsCQFgM5iOFFbmw4u2wpn8D0a2vZmR6KbVHtDiNViDkdnBOZrsnIoB0lzg_BFNgOg0HW3JEH9y-nk133c8mbRrVhy3ImmuSXPj3mwWEl7WEI4yzw1Ri5PU-RNnJmJheb6ca-p5qpsj_anUPGZlnYZGuJau1U-KeLLnZoVbAFqWmfm__1D24iq8MVMeX7brWL7G0ZamoA-1AchZ6WUgXcYZ0qH4bdlSSiRs8xb47DBFzAyikdlTaMMXrpxmhmLB7hlpYe3abxhw8IvoOHHDswaeeofWR8fYWxnGP3aabJ24A0GLdJCz9kcb1GarT4Wq0cOeiDP9WQ");
            //  httpPost.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType() + ";boundary=" + boundary);
            //    HttpResponse response = httpClient.execute(httpPost);
            //  HttpEntity result = response.getEntity();
            // String rsp =inputStreamToString(response.getEntity().getContent());
            //  System.out.println(rsp); */
            //  }
        }
    }

    public void downloadFile(String url, File file) throws IOException {
        file = new File("/Users/mingxuan/Desktop/[RSL CORP] USS Express Pass_281120192.pdf");

        CloseableHttpClient client = HttpClients.createDefault();
        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (FileOutputStream outstream = new FileOutputStream(file)) {
                    entity.writeTo(outstream);
                }
            }
        }
    }

    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {

        }

        // Return full string
        return total.toString();
    }
}
