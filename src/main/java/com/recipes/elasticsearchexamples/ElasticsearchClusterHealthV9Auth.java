package com.recipes.elasticsearchexamples;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.HealthRequest;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.recipes.config.ElasticsearchConnection;

@Component
public class ElasticsearchClusterHealthV9Auth implements CommandLineRunner {

  private ElasticsearchConnection elasticsearchConnection;
  private Logger logger = LoggerFactory.getLogger(ElasticsearchClusterHealthV9Auth.class);

  public ElasticsearchClusterHealthV9Auth(
      ElasticsearchConnection elasticsearchConnection) {

    this.elasticsearchConnection = elasticsearchConnection;

  }

  public void logClusterHealth() {
    try (ElasticsearchClient esClient = this.elasticsearchConnection.getClient()) {

      logger.info("esClient ping : {}", esClient.ping());

      // 5. Create and execute the Cluster Health request
      HealthRequest healthRequest = new HealthRequest.Builder().build();
      HealthResponse healthResponse = esClient.cluster().health(healthRequest);

      // 6. Process the response (same as before)
      System.out.println("--- Elasticsearch Cluster Health (v9.0.1) with Auth ---");
      System.out.println("Cluster Name: " + healthResponse.clusterName());
      System.out.println("Status: " + healthResponse.status());
      System.out.println("Number of Nodes: " + healthResponse.numberOfNodes());
      System.out.println("Number of Data Nodes: " + healthResponse.numberOfDataNodes());
      System.out.println("Active Primary Shards: " + healthResponse.activePrimaryShards());
      System.out.println("Active Shards: " + healthResponse.activeShards());
      System.out.println("Relocating Shards: " + healthResponse.relocatingShards());
      System.out.println("Initializing Shards: " + healthResponse.initializingShards());
      System.out.println("Unassigned Shards: " + healthResponse.unassignedShards());
      System.out.println("Delayed Unassigned Shards: " + healthResponse.delayedUnassignedShards());
      System.out.println("Number of Pending Tasks: " + healthResponse.numberOfPendingTasks());
      System.out.println("Number of In Flight Fetch: " + healthResponse.numberOfInFlightFetch());
      System.out.println("Task Max Waiting In Queue: " + healthResponse.taskMaxWaitingInQueue());
      System.out.println("Active Shards Percent As String: " + healthResponse.activeShardsPercentAsNumber() + "%");

      switch (healthResponse.status()) {
        case Red:
          System.err.println("WARNING: Cluster health is RED! Immediate attention required.");
          break;
        case Yellow:
          System.out.println("INFO: Cluster health is YELLOW. Some replicas are not assigned.");
          break;
        case Green:
          System.out.println("SUCCESS: Cluster health is GREEN. All shards are assigned.");
          break;
      }

    } catch (Exception e) {
      // System.err.println("Error while communicating with Elasticsearch: " +
      // e.getMessage());
      logger.error("Error while communicating with elasticsearch, class : {}, message : {}", e.getClass(),
          e.getMessage());
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
  }

  @Override
  public void run(String... args) throws Exception {
    // this.logClusterHealth();
  }
}