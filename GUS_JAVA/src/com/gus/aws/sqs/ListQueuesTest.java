package com.gus.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ListQueuesTest extends AbstractSQSTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		AmazonSQS sqs = getAmazonSQS();
		
        ListQueuesResult result = sqs.listQueues();
        
        List<String> qUrls = result.getQueueUrls();
        if (qUrls != null && !qUrls.isEmpty()) {
        	System.out.println("Queue URLs:");
        	for (String qUrl : qUrls) {
				System.out.println(qUrl);
			}
        }
	}

}
