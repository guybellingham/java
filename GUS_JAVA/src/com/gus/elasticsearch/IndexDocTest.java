package com.gus.elasticsearch;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;
import java.io.IOException;


public class IndexDocTest {
	
	static final String serviceName = "es";
	static final String region = "us-west-2";
    static final String aesEndpoint = "https://vpc-movies-4ehtlmcjzw5inyr5t7g7coyrvy.us-west-2.es.amazonaws.com";
    
    static final String index = "movie";
    static final String type = "_doc";
    static final String id = "1";
    
    static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
    
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		RestHighLevelClient esClient = esHighLevelClient(serviceName, region);
		 
        // Create the document as a hash map
        Map<String, Object> document = new HashMap();
        document.put("title", "Walk the Line");
        document.put("director", "James Mangold");
        document.put("year", "2005");
 
        // Form the indexing request, send it, and print the response
        IndexRequest request = new IndexRequest(index, type, id).source(document);
        IndexResponse response;
		try {
			response = esClient.index(request);
			System.out.println(response.toString());
		} catch (IOException e) {
			System.out.println("IndexRequest FAILED with exception "+e);
		}
        
	}
	
	/**
	 *  Adds the interceptor to the ES REST client
	 * @param serviceName
	 * @param region
	 * @return
	 */
    public static RestClient esClient(String serviceName, String region) {
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
        return RestClient.builder(HttpHost.create(aesEndpoint))
        		.setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor))
        		.build();
    }
    /**
     *  Adds the interceptor to the ES RestHighLevelClient
     * @param serviceName
     * @param region
     * @return
     */
    public static RestHighLevelClient esHighLevelClient(String serviceName, String region) {
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint))
        		.setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }
}
