package com.gus.aws.sqs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractSQSTest {
	
	protected static final Logger logger = Logger.getLogger("com.innotas.aws.sqs");
	
	protected static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
	
	protected static final String Q_URL = "https://sqs.us-west-2.amazonaws.com/252218482207/r2-devvm-gus.fifo";
	
	private static final String JDBC_URL = "jdbc:postgresql://devvm:5432/devvm";
	private static final String USERNAME = "arena";
	private static final String PASSWORD = "innotas";
	
	static final int DEFAULT_CONNECTION_COUNT = 50;
	
	public AmazonSQS getAmazonSQS() {
		return getAmazonSQS(DEFAULT_CONNECTION_COUNT);
	}
	
	public AmazonSQS getAmazonSQS(int connectionCount) {
		final ClientConfiguration clientConfiguration = new ClientConfiguration()
                .withMaxConnections(connectionCount);
		AmazonSQS sqs = AmazonSQSClientBuilder.standard()
				  .withCredentials(credentialsProvider)
				  .withRegion(Regions.US_WEST_2)
				  .withClientConfiguration(clientConfiguration)
				  .build();
		return sqs;
	}
	
	public Connection getConnection() throws SQLException {
		return getConnection(JDBC_URL, USERNAME, PASSWORD);
	}
	public Connection getConnection(String jdbcURL, String userName, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(jdbcURL, userName, password);
        conn.setReadOnly(true);
        conn.setAutoCommit(false);
        setAdminContext(conn);
        return conn;
    }
	
    public void setAdminContext(Connection conn) throws SQLException {
        Statement s = conn.createStatement();
        try {
            s.execute("select arena.set_pa_ctxt('admin', null)");
        }
        finally {
            s.close();
        }
    }
    
	public Map<String,String> getQueueAttributes() {
		return getQueueAttributes(Q_URL);
	}
	public Map<String,String> getQueueAttributes(String queueUrl) {
		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(queueUrl)
		    .withAttributeNames("All");
		GetQueueAttributesResult getQueueAttributesResult = getAmazonSQS().getQueueAttributes(getQueueAttributesRequest);
		return getQueueAttributesResult.getAttributes();
	}
}
