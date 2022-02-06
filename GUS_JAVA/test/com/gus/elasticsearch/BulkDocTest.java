package com.gus.elasticsearch;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BulkDocTest {
	
	static final String serviceName = "es";
	static final String region = "us-west-2";
    static final String aesEndpoint = "https://vpc-movies-4ehtlmcjzw5inyr5t7g7coyrvy.us-west-2.es.amazonaws.com";
    
    static final String indexName = "movie";
    static final String type = "_doc";
    static final String fileName = "C:\\Temp\\sample-movies.json";
    
    static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
    
	@Before
	public void setUp() throws Exception {
		org.apache.log4j.LogManager.getRootLogger();

	}

	@Test
	public void test() {
		RestHighLevelClient esClient = esHighLevelClient(serviceName, region);
		
        Response response;
		try {
			response = esClient.getLowLevelClient().performRequest("HEAD", "/" + indexName);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("HEAD request for \""+indexName+"\" returned statusCode="+statusCode);
		} catch (IOException e) {
			System.out.println("FAILED HEAD request exception="+e);
		}
        
		BulkRequest bulkRequest = new BulkRequest();
		BulkListener listener = new BulkListener();
		
		BulkProcessor bulkProcessor = BulkProcessor.builder(esClient::bulkAsync, listener).build();
		
        BufferedReader br = null;
        try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("FAILED to find input file \""+fileName+"\" exception="+e);
			return;
		}

        if (br != null) {
        	try {
        		String line;

                while ((line = br.readLine()) != null) {
                    bulkProcessor.add(new IndexRequest(indexName, type).source(line, XContentType.JSON));
                }

                System.out.println("Waiting to finish...");

                boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
                if(!terminated) {
                    System.out.println("Some requests have not been processed");
                }

			} catch (IOException e1) {
				System.out.println("FAILED to read input file \""+fileName+"\" exception="+e1);
			} catch (InterruptedException e2) {
				System.out.println("FAILED to awaitClose() exception="+e2);
			} finally {
	            try {
					esClient.close();
					System.out.println("Done.");
				} catch (IOException e) {
					System.out.println("FAILED to close() client exception="+e);
				}
			}
        	
        }
        
//		IndexRequest indexRequest = new IndexRequest(index, type, "4");
//		indexRequest.source("title", "Alien", "director", "Ridley Scott", "year", 1979, "writer", "Dan O'Bannon", "stars", new String[]{"Sigourney Weaver","Tom Skerrit","John Hurt"});
//		bulkRequest.add(indexRequest);
//		indexRequest = new IndexRequest(index, type, "5");
//		indexRequest.source("title", "Aliens", "director", "James Camreon", "year", 1986, "stars", new String[]{"Sigourney Weaver","Michael Biehn","Carrie Henn","Lance Henriksen","Paul Reiser"});
//		bulkRequest.add(indexRequest);
//		indexRequest = new IndexRequest(index, type, "5");
//		indexRequest.source("title", "Alien3", "director", "David Fincher", "year", 1992, "stars", new String[]{"Sigourney Weaver","Charles Dance","Charles S Dutton"});
//		bulkRequest.add(indexRequest);
//		BulkListener listener = new BulkListener();
//		esClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, listener);	
        
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
    
    static class BulkListener implements BulkProcessor.Listener {
    	int count = 0;

        @Override
        public void beforeBulk(long l, BulkRequest bulkRequest) {
            count = count + bulkRequest.numberOfActions();
            System.out.println("Uploaded " + count + " so far");
        }

        @Override
        public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()) {
                        System.out.println(bulkItemResponse.getOpType());
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        System.out.println("Error " + failure.toString());
                    }
                }
            }
        }

        @Override
        public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
            System.out.println("Big errors " + throwable.toString());
        }
    }
}
