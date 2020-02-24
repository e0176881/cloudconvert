/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

@Named(value = "indexManagedBean")
@ViewScoped
public class IndexManagedBean implements Serializable {

    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {

    }

    public void createJob() throws Exception {
        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(40);
        cm.setMaxTotal(500);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setSSLSocketFactory(sslsf)
                .build();

        // HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://api.cloudconvert.com/v2/jobs");
        // HttpPost request20 = new HttpPost("https://api.cloudconvert.com/v2/optimize");
        JSONObject object = new JSONObject();
        JSONObject tasks = new JSONObject();
        JSONObject import1 = new JSONObject();
        import1.put("operation", "import/upload");
        tasks.put("import-1", import1); //end of import-1

//         JSONObject input = new JSONObject();
//         input.put("input",import1 );
        JSONArray import1Array = new JSONArray();
        //import1Array.put(input);
        import1Array.put("import-1");

        JSONObject task1 = new JSONObject();
        task1.put("operation", "convert");
        task1.put("input_format", "docx");
        task1.put("output_format", "pdf");
        task1.put("engine", "libreoffice");
        task1.put("input", import1Array);
        tasks.put("task-1", task1); //end of task-1

        JSONArray task1Array = new JSONArray();
        //import1Array.put(input);
        task1Array.put("task-1");

        JSONObject task2 = new JSONObject();
        task2.put("operation", "optimize");
        task2.put("input", task1Array);
        task2.put("input_format", "pdf");
        task2.put("engine", "3heights");
        task2.put("profile", "web");
        task2.put("engine_version", "6.2");
        tasks.put("task-2", task2); //end of task-2
        JSONArray task2Array = new JSONArray();
        task2Array.put("task-2");

        JSONObject export1 = new JSONObject();
        export1.put("operation", "export/url");
        export1.put("input", task2Array);
        export1.put("inline", false);
        export1.put("archive_multiple_files", false);
        tasks.put("export-1", export1); //end of export-1

        object.put("tasks", tasks);
        object.put("tag", "createdTag");

        StringEntity params = new StringEntity(object.toString());
        request.setEntity(params);
        request.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk0OGViZTBiZGU1ZTdhMDc4M2VhZjVkZjYxZWRlNTUxOTNiYWU3MjViM2Y0ODUzZGNjMjc5Mjg3ODlmYjU3NDg2ZWUwMjg0NzE0ZjZlY2UxIn0.eyJhdWQiOiIxIiwianRpIjoiOTQ4ZWJlMGJkZTVlN2EwNzgzZWFmNWRmNjFlZGU1NTE5M2JhZTcyNWIzZjQ4NTNkY2MyNzkyODc4OWZiNTc0ODZlZTAyODQ3MTRmNmVjZTEiLCJpYXQiOjE1ODIyOTg1NDIsIm5iZiI6MTU4MjI5ODU0MiwiZXhwIjo0NzM3OTcyMTQyLCJzdWIiOiI0MDQ2MTIzNyIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.YB2HdA00K9JTZB1X78tdFHyjxCPtpKYhu1OcNGrHw2jqAlA2EYRECXIGPzgsjpGrhjZT3VEpBU9pnk8FVyIf07pnxgsQ9ZgfAE7Zgd25r5J9FLwrHYB8GINWX8IoZE39-W_wkBEFSmuuE1niF22Q7aFStHesMQ1svjPu6ftRWHHGxv1sBNn1XvB62oxXRQA3ijznKDjKr8tSuexZ6exLSxzaDd_awoCTma_3v1hpSE7k3EbE5IOGjTZjGTKRrnwyKXjiMqf4kddwNKMTqq6RoXPZ5o0LIlNwxlp9wD89q8bO7qnECNPx8QLq-RngpCWWqD6gJSV8YrjeHgx3cM0UcdEE9nKFMT-DCVtQpiHjNh2WoUeML1QIF5asTrE4FiAk7beHTMwOSymym-8plG58M3I74hBmEl_93Or0xrCVA9nsVLcJhxlWCG9_R5T0mQRsmOt9kIrrMNRiFoQ8uZfBV9EPyVPpkhnBk_2RccVYXMgAo-dPQH3kmsH0dYn8fpPi1shJwfIkcQjvpJKuGErWAQeBmjxr0c9mgk9JNsVJ2X_AyZjAEX3SqVnarK42o7klVCylQqzzm6tFan2P_4jy2flmuOzLXnv_dcfIL_nztaxx_FAE7wHkUKR-IYqm27eor0gWFUSNZW09LfgPOZ-eVP7_P4lNKgp1tIigaCquK1M");
        request.setHeader("Content-type", "application/json");

        System.out.println("Executing request " + request.getRequestLine());

        // Create a custom response handler
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpClient.execute(request, responseHandler);
        System.out.println("-----////------------------------\\\\-----------");
        System.out.println(responseBody);

        JSONObject result = new JSONObject(responseBody);
        JSONArray resultTaskArray = result.getJSONObject("data").getJSONArray("tasks");
        JSONObject importTaskObject = null;
        JSONObject convertObject = null;
        JSONObject compressObject = null;
        JSONObject exportObject = null;
        String exportId = null;
        for (int i = 0; i < resultTaskArray.length(); i++) {
            if (resultTaskArray.getJSONObject(i).getString("name").equals(import1Array.getString(0))) {
                importTaskObject = resultTaskArray.getJSONObject(i);
//                String importTaskId = resultTaskArray.getJSONObject(i).getString("id");
//                System.out.println("importTaskId : " + importTaskId);
//                JSONObject form = resultTaskArray.getJSONObject(i).getJSONObject("result").getJSONObject("form");
//                JSONObject para = gss.getJSONObject("data").getJSONObject("result").getJSONObject("form").getJSONObject("parameters");
//                String redirect = para.getString("redirect");
//                String signature = para.getString("signature");
//                int expireString = para.getInt("expires");
//                int maxFileSize = para.getInt("max_file_size");
//                int maxFileCount = para.getInt("max_file_count");
//                String uploadURL = form.getString("url");
//                String jobId = gss.getJSONObject("data").getString("id");
//                System.out.println("url " + uploadURL);
//                System.out.println("expire " + expireString);
//                System.out.println("maxFileSize " + maxFileSize);
//                System.out.println("maxFileCount " + maxFileCount);
//                System.out.println("redirect " + redirect);
//                System.out.println("signature " + signature);

            } else if (resultTaskArray.getJSONObject(i).getString("name").equals(task1Array.getString(0))) {
                convertObject = resultTaskArray.getJSONObject(i);
                String convertId = convertObject.getString("id");
                System.out.println("ConvertId : " + convertId);
            } else if (resultTaskArray.getJSONObject(i).getString("name").equals(task2Array.getString(0))) {
                compressObject = resultTaskArray.getJSONObject(i);
                String compressId = compressObject.getString("id");
                System.out.println("compressId : " + compressId);
            } else {
                exportObject = resultTaskArray.getJSONObject(i);
                exportId = exportObject.getString("id");

                System.out.println("exportId : " + exportId);
            }
        }
        import_upload(importTaskObject, exportId);

    }

    public void import_upload(JSONObject importTaskObject, String exportId) throws Exception {

        String importTaskId = importTaskObject.getString("id");
        System.out.println("importTaskId : " + importTaskId);
        JSONObject form = importTaskObject.getJSONObject("result").getJSONObject("form");
        JSONObject para = form.getJSONObject("parameters");
        String uploadURL = form.getString("url");
        String redirect = para.getString("redirect");
        String signature = para.getString("signature");
        int expireString = para.getInt("expires");
        int maxFileSize = para.getInt("max_file_size");
        int maxFileCount = para.getInt("max_file_count");
        System.out.println("url " + uploadURL);
        System.out.println("expire " + expireString);
        System.out.println("maxFileSize " + maxFileSize);
        System.out.println("maxFileCount " + maxFileCount);
        System.out.println("redirect " + redirect);
        System.out.println("signature " + signature);

        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        String boundary = Long.toHexString(System.currentTimeMillis());

        File file = new File("C:\\Users\\User\\Desktop\\funn.docx");

        HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setBoundary(boundary)
                .addTextBody("expires", Integer.toString(expireString))
                .addTextBody("max_file_count", Integer.toString(maxFileCount))
                .addTextBody("max_file_size", "10000000000")
                .addTextBody("redirect", redirect)
                .addTextBody("signature", signature)
                .addBinaryBody("file", new FileInputStream(file), ContentType.DEFAULT_BINARY, file.getName())
                .build();

        HttpUriRequest newRequest = RequestBuilder
                .post(uploadURL)
                .setEntity(entity)
                .setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjgxNDkxNWNmZDFmY2Y4NjgyZTYzYTcxMjgzMmRlZjc5Mjc4NjJmODRmYTdiN2QwNDYxOTBjNDRkYzk1NmJmZTY4ZDVkNTZlY2FkMmMyMjVkIn0.eyJhdWQiOiIxIiwianRpIjoiODE0OTE1Y2ZkMWZjZjg2ODJlNjNhNzEyODMyZGVmNzkyNzg2MmY4NGZhN2I3ZDA0NjE5MGM0NGRjOTU2YmZlNjhkNWQ1NmVjYWQyYzIyNWQiLCJpYXQiOjE1ODIyNzIyMjQsIm5iZiI6MTU4MjI3MjIyNCwiZXhwIjo0NzM3OTQ1ODI0LCJzdWIiOiIzOTgyODc4OSIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay53cml0ZSIsInRhc2sucmVhZCIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.ov2FqKmBhQPA3poQoavkGjiMdUimXq1eh9OTY7zzFW0JBmpSlHNnwjjA7NwbSKdUqhhevXC3opbMh-HaBY8nlkJfhyO17Bkj0omMKudF68LJ8rcLjuz0w1GHzFuIxmq5Yb9ozPcsvr7p1oK2x4F_-qF9sdmlkBkQ1Ryrh3zZUx4Y_vgQB44ScTwRdcc0wQYn_1aQTOWHNwrqI7YFQhil-qcWqK8i7UozPQtoguWoEupvyGCvbMDtIIne1GbSA3BqE15BOlRLMygLwnlLiEXxNmlvhYmgcaEoNQdDl1E0UkzdAvCdEE_5Gl2fp1opZFGf-qlBZ8FKxobXc7mil1VLHvU74r6qId4JtCl6Vd4qHGi2JpD5mC7LN9yu3LJ5m1m_-3LG4g1T-nsIIFsI32o0hEJm2EJEA6AAj0LDqPR4QXSzI7utS1W_KCnNg1s_EuocW4NNSLQDcBGdatvQz1oSQ6GEIejMmDIbhpgsFxI_1lSZNxbz1u6ekLCffBqyHMpOokP7WpeARCoYOPjFkXaixEGrXq0UyhHeeBtZ7FIp5DyOzkA7XQ9fcbWX2QpHKUtOgtBS6kOVJzG18CSnFsb-jxtcQjudbPAdjN8Dv2DtE7kzp9vI_u71IOIIqy11Np3qmAaNQOK9jUocFJjbAEsMypqyTlnT41Nba91hkNdOQQM")
                .setHeader("Content-type", "application/json")
                .setHeader("Content-Type", "multipart/form-data" + ";boundary=" + boundary)
                .build();

        System.out.println("Executing request " + newRequest.getRequestLine());

        // Create a custom response handler
        ResponseHandler<String> newResponseHandler = newResponse -> {
            int status = newResponse.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity httpEntity = newResponse.getEntity();
                return httpEntity != null ? EntityUtils.toString(httpEntity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String newResponseBody = httpClient.execute(newRequest, newResponseHandler);
        System.out.println("----------------------------------------");
        System.out.println(newResponseBody);

        waitTaskJob(exportId);
    }

    public void waitTaskJob(String exportId) throws Exception {
        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        HttpGet request = new HttpGet("https://api.cloudconvert.com/v2/tasks/" + exportId + "/wait");

        request.setHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk0OGViZTBiZGU1ZTdhMDc4M2VhZjVkZjYxZWRlNTUxOTNiYWU3MjViM2Y0ODUzZGNjMjc5Mjg3ODlmYjU3NDg2ZWUwMjg0NzE0ZjZlY2UxIn0.eyJhdWQiOiIxIiwianRpIjoiOTQ4ZWJlMGJkZTVlN2EwNzgzZWFmNWRmNjFlZGU1NTE5M2JhZTcyNWIzZjQ4NTNkY2MyNzkyODc4OWZiNTc0ODZlZTAyODQ3MTRmNmVjZTEiLCJpYXQiOjE1ODIyOTg1NDIsIm5iZiI6MTU4MjI5ODU0MiwiZXhwIjo0NzM3OTcyMTQyLCJzdWIiOiI0MDQ2MTIzNyIsInNjb3BlcyI6WyJ1c2VyLnJlYWQiLCJ1c2VyLndyaXRlIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.YB2HdA00K9JTZB1X78tdFHyjxCPtpKYhu1OcNGrHw2jqAlA2EYRECXIGPzgsjpGrhjZT3VEpBU9pnk8FVyIf07pnxgsQ9ZgfAE7Zgd25r5J9FLwrHYB8GINWX8IoZE39-W_wkBEFSmuuE1niF22Q7aFStHesMQ1svjPu6ftRWHHGxv1sBNn1XvB62oxXRQA3ijznKDjKr8tSuexZ6exLSxzaDd_awoCTma_3v1hpSE7k3EbE5IOGjTZjGTKRrnwyKXjiMqf4kddwNKMTqq6RoXPZ5o0LIlNwxlp9wD89q8bO7qnECNPx8QLq-RngpCWWqD6gJSV8YrjeHgx3cM0UcdEE9nKFMT-DCVtQpiHjNh2WoUeML1QIF5asTrE4FiAk7beHTMwOSymym-8plG58M3I74hBmEl_93Or0xrCVA9nsVLcJhxlWCG9_R5T0mQRsmOt9kIrrMNRiFoQ8uZfBV9EPyVPpkhnBk_2RccVYXMgAo-dPQH3kmsH0dYn8fpPi1shJwfIkcQjvpJKuGErWAQeBmjxr0c9mgk9JNsVJ2X_AyZjAEX3SqVnarK42o7klVCylQqzzm6tFan2P_4jy2flmuOzLXnv_dcfIL_nztaxx_FAE7wHkUKR-IYqm27eor0gWFUSNZW09LfgPOZ-eVP7_P4lNKgp1tIigaCquK1M");
        // request.setEntity(entity);

        System.out.println("Executing request " + request.getRequestLine());
        File file = new File("C:\\Users\\User\\Desktop\\funn.pdf");
        // Create a custom response handler
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpClient.execute(request, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        JSONObject responseBodyObject = new JSONObject(responseBody);
        JSONArray filesArray = responseBodyObject.getJSONObject("data").getJSONObject("result").getJSONArray("files");
        System.out.println(filesArray);
        String downloadLink = filesArray.getJSONObject(0).getString("url");
        System.out.println("downloadlink" + downloadLink);
        downloadFile(downloadLink, file);
    }

    public void downloadFile(String url, File file) throws Exception {
        file = new File("C:\\Users\\User\\Desktop\\funn123.pdf");

        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );
        CloseableHttpClient client = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (FileOutputStream outstream = new FileOutputStream(file)) {
                    entity.writeTo(outstream);
                }
            }
        }
    }

}
