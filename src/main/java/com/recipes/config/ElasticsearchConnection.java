package com.recipes.config;

import java.io.IOException;
import java.util.Base64;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PreDestroy;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ElasticsearchConnection {
  private final String host, username, password;
  private final int port;
  private final Logger logger = LoggerFactory.getLogger(ElasticsearchConnection.class);
  private ElasticsearchClient esClient;
  private RestClient restClient;

  public ElasticsearchConnection(@Value("${spring.elasticsearch.host}") String host,
      @Value("${spring.elasticsearch.port}") int port,
      @Value("${spring.elasticsearch.username}") String username,
      @Value("${spring.elasticsearch.password}") String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    this.initializeClient();
  }

  private void initializeClient() {
    try {
      this.restClient = RestClient.builder(
          new HttpHost(this.host, this.port, "http"))
          .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
              .setConnectTimeout(5000)
              .setSocketTimeout(60000))
          .setDefaultHeaders(new Header[] {
              new BasicHeader("Authorization", "Basic " +
                  Base64.getEncoder().encodeToString(
                      (this.username + ":" + this.password).getBytes()))
          })
          .build();

      ElasticsearchTransport transport = new RestClientTransport(
          this.restClient, new JacksonJsonpMapper());
      this.esClient = new ElasticsearchClient(transport);

    } catch (Exception e) {
      logger.error("Failed to initialize Elasticsearch client: {}", e.getMessage());
      throw new RuntimeException("Failed to initialize Elasticsearch client", e);
    }
  }

  public ElasticsearchClient getClient() {
    return this.esClient;
  }

  @PreDestroy
  public void cleanup() {
    try {
      if (this.restClient != null) {
        this.restClient.close();
      }
    } catch (IOException e) {
      logger.error("Error closing Elasticsearch client: {}", e.getMessage());
    }
  }
}

// @Component
// @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
// public class ElasticsearchConnection {
// private final String host, username, password;
// private final int port;
// private final Logger logger =
// LoggerFactory.getLogger(ElasticsearchConnection.class);

// public ElasticsearchConnection(@Value("${spring.elasticsearch.host}") String
// host,
// @Value("${spring.elasticsearch.port}") int port,
// @Value("${spring.elasticsearch.username}") String username,
// @Value("${spring.elasticsearch.password}") String password) {
// this.host = host;
// this.port = port;
// this.username = username;
// this.password = password;
// // ElasticsearchConnection.esClient = this.createClient();
// logger.info("username: {}, password: {}", username, password);
// }

// private ElasticsearchClient createClient() {
// final String ELASTIC_USERNAME = this.username;
// final String ELASTIC_PASSWORD = this.password;

// try (RestClient restClient = RestClient.builder(
// new HttpHost("localhost", 9200, "http"))
// .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
// .setConnectTimeout(5000)
// .setSocketTimeout(60000))
// .setDefaultHeaders(new Header[] {
// new BasicHeader("Authorization", "Basic " +
// Base64.getEncoder().encodeToString(
// (ELASTIC_USERNAME + ":" + ELASTIC_PASSWORD).getBytes()))
// })
// .build()) {

// ElasticsearchTransport transport = new RestClientTransport(
// restClient, new JacksonJsonpMapper());
// ElasticsearchClient esClient = new ElasticsearchClient(transport);
// logger.info("esClient connection ready to be established through
// elasticsearchConnection");
// logger.info("esClient ping : {}", esClient.ping().value());

// return esClient;
// } catch (Exception e) {
// logger.info("unable to connect to elasticsearch client, message : {}",
// e.getMessage());
// return null;

// }
// }

// public ElasticsearchClient getClient() {
// return this.createClient();
// }

// public static void main(String[] args) {
// // ElasticsearchConnection eConnection = new ElasticsearchConnection("",
// 9200,
// // "elastic", "fzWMc4DH");

// final String ELASTIC_USERNAME = "elastic"; // Common default user
// final String ELASTIC_PASSWORD = "fzWMc4DH"; // **CHANGE THIS**

// // 2. Create the Low-Level RestClient (from org.elasticsearch.client)
// RestClient restClient = null;
// try {
// restClient = RestClient.builder(
// new HttpHost("localhost", 9200, "http")) // Or "https" if your cluster uses
// SSL
// .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
// .setConnectTimeout(5000)
// .setSocketTimeout(60000))
// .setDefaultHeaders(new Header[] {
// new BasicHeader("Authorization", "Basic " +
// Base64.getEncoder().encodeToString(
// (ELASTIC_USERNAME + ":" + ELASTIC_PASSWORD).getBytes()))
// })
// .build();

// // 3. Create the Transport layer
// ElasticsearchTransport transport = new RestClientTransport(
// restClient, new JacksonJsonpMapper());

// // 4. Create the ElasticsearchClient (the new Java API Client)
// ElasticsearchClient esClient = new ElasticsearchClient(transport);

// // 5. Create and execute the Cluster Health request
// HealthRequest healthRequest = new HealthRequest.Builder().build();
// HealthResponse healthResponse = esClient.cluster().health(healthRequest);

// // 6. Process the response (same as before)
// System.out.println("--- Elasticsearch Cluster Health (v9.0.1) with Auth
// ---");
// System.out.println("Cluster Name: " + healthResponse.clusterName());
// System.out.println("Status: " + healthResponse.status());
// System.out.println("Number of Nodes: " + healthResponse.numberOfNodes());
// System.out.println("Number of Data Nodes: " +
// healthResponse.numberOfDataNodes());
// System.out.println("Active Primary Shards: " +
// healthResponse.activePrimaryShards());
// System.out.println("Active Shards: " + healthResponse.activeShards());
// System.out.println("Relocating Shards: " +
// healthResponse.relocatingShards());
// System.out.println("Initializing Shards: " +
// healthResponse.initializingShards());
// System.out.println("Unassigned Shards: " +
// healthResponse.unassignedShards());
// System.out.println("Delayed Unassigned Shards: " +
// healthResponse.delayedUnassignedShards());
// System.out.println("Number of Pending Tasks: " +
// healthResponse.numberOfPendingTasks());
// System.out.println("Number of In Flight Fetch: " +
// healthResponse.numberOfInFlightFetch());
// System.out.println("Task Max Waiting In Queue: " +
// healthResponse.taskMaxWaitingInQueue());
// System.out.println("Active Shards Percent As String: " +
// healthResponse.activeShardsPercentAsNumber() + "%");

// switch (healthResponse.status()) {
// case Red:
// System.err.println("WARNING: Cluster health is RED! Immediate attention
// required.");
// break;
// case Yellow:
// System.out.println("INFO: Cluster health is YELLOW. Some replicas are not
// assigned.");
// break;
// case Green:
// System.out.println("SUCCESS: Cluster health is GREEN. All shards are
// assigned.");
// break;
// }
// esClient.close();
// } catch (IOException e) {
// System.err.println("Error while communicating with Elasticsearch: " +
// e.getMessage());
// e.printStackTrace();
// } finally {
// // 7. Close the RestClient
// try {
// if (restClient != null) {
// restClient.close();

// }
// } catch (IOException e) {
// System.err.println("Error closing Elasticsearch client: " + e.getMessage());
// e.printStackTrace();
// }
// }
// }

// }
