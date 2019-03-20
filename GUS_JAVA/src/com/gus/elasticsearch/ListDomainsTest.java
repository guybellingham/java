package com.gus.elasticsearch;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticsearch.AWSElasticsearch;
import com.amazonaws.services.elasticsearch.AWSElasticsearchClientBuilder;
import com.amazonaws.services.elasticsearch.model.DomainInfo;
import com.amazonaws.services.elasticsearch.model.ListDomainNamesRequest;
import com.amazonaws.services.elasticsearch.model.ListDomainNamesResult;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ListDomainsTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		final String serviceEndpoint = "vpc-movies-4ehtlmcjzw5inyr5t7g7coyrvy.us-west-2.es.amazonaws.com";
		final String signingRegion = "us-west-2";
		//EndpointConfiguration endpointConfiguration = new EndpointConfiguration(serviceEndpoint, signingRegion);
        // Build the client using the default credentials chain.
        // You can use the AWS CLI and run `aws configure` to set access key, secret
        // key, and default region.
        final AWSElasticsearch client = AWSElasticsearchClientBuilder
                .standard()
                // Unnecessary, but lets you use a region different than your default.
                .withRegion(Regions.US_WEST_2)
                // Unnecessary, but if desired, you can use a different provider chain.
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                //.withEndpointConfiguration(endpointConfiguration)   <-- gives server error:500 and no explanation?  :(
                .build();
        ListDomainNamesRequest listDomainNamesRequest = new ListDomainNamesRequest();
        System.out.println("Requesting domain names from "+serviceEndpoint+" ...");
        ListDomainNamesResult result = client.listDomainNames(listDomainNamesRequest);
        List<DomainInfo> domains = result.getDomainNames();
        if (domains != null) {
        	System.out.println("Domain Names:");
        	for (DomainInfo domainInfo : domains) {
				System.out.println(domainInfo.getDomainName());
			}
        }
	}

}
