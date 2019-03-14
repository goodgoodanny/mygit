package test;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

/**
 * http客户端
 * 
 */
public class HttpClientUtil {
	/**
	 * Get方式调用http接口（入参：JSON）
	 *
	 * @param url
	 *            求地址
	 * @param params
	 *            请求参数 Key-value
	 * @param exHeaders
	 *            扩展请求头，例如token信息
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static <T> T doGet(String url, Map<String, Object> params, Map<String, String> exHeaders, Class<T> type,String contentType)
			throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Content-Type", contentType);

		if (null != exHeaders) {
			for (Map.Entry<String, String> header : exHeaders.entrySet()) {
				httpGet.addHeader(header.getKey(), header.getValue());
			}
		}


		CloseableHttpResponse response = client.execute(httpGet);//
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, "UTF-8");
		Gson gson = new Gson();
		return gson.fromJson(result, type);
	}

	public static void main(String[] args) throws IllegalArgumentException {

	String a ="https://www.baidu.com";
String	 ss=doGet(a);
		
		System.out.println(ss);
	}
	public static String doGet(String url)
	  {
	    String result = "";
	    try {
	      HttpClient httpClient = getHttpClient();

	      HttpGet httpGet = new HttpGet(url);
	      HttpResponse response = httpClient.execute(httpGet);//-------org.apache.http.client.ClientProtocolException以及org.apache.http.ProtocolException

	      if (response.getStatusLine().getStatusCode() == 200)
	      {
	        result = EntityUtils.toString(response.getEntity());
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return result;
	  }
	 public static CloseableHttpClient getHttpClient() {
		    SSLContext sslContext;
		    try {
		      sslContext = SSLContext.getInstance("SSL");
		      sslContext.init(null, new TrustManager[] { new X509TrustManager()
		      {
		        public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
		          throws CertificateException
		        {
		        }

		        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
		        {
		        }

		        public X509Certificate[] getAcceptedIssuers()
		        {
		          return new X509Certificate[0];
		        }
		      }
		       }, new SecureRandom());

		      SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		      CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();

		      return closeableHttpClient;
		    } catch (Exception e) {
		      System.out.println("create closeable http client failed!"); }
		    return HttpClientBuilder.create().build();
		  }

}
