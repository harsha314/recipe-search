package com.recipes.data;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.recipes.config.ElasticsearchConnection;
import com.recipes.dto.RecipeDTO;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;

@Component
public class DataImport implements CommandLineRunner {

  Logger logger = LoggerFactory.getLogger(DataImport.class);

  private ElasticsearchConnection elasticsearchConnection;

  public DataImport(ElasticsearchConnection elasticsearchConnection) {
    this.elasticsearchConnection = elasticsearchConnection;
  }

  @Override
  public void run(String... args) throws Exception {
    // this.importData();
    // this.fetchCountOfRecipesIndex();
  }

  private void importData() {
    File file = Paths.get("src", "main", "resources", "recipes.csv").toFile();
    try (Reader reader = new FileReader(file)) {
      Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.builder()
          .setHeader("id", "recipe_name", "prep_time", "cook_time", "total_time", "servings", "yield", "ingredients",
              "directions", "rating", "url", "cuisine_path", "nutrition", "timing", "img_src")
          .setSkipHeaderRecord(true)
          .build()
          .parse(reader);
      List<CSVRecord> recipes = new ArrayList<>();
      for (CSVRecord csvRecord : csvRecords) {
        recipes.add(csvRecord);
      }
      recipes = recipes.stream().limit(1000).toList();
      this.moveToElasticsearch(recipes);
    } catch (Exception e) {
      logger.info("exception occured importing data");
      logger.info(e.getMessage());
    }
  }

  private void moveToElasticsearch(List<CSVRecord> recipes) {
    List<RecipeDTO> recipeDTOs = recipes.stream().map(csvRecord -> {
      int id = Integer.parseInt(csvRecord.get("id"));
      String recipeName = csvRecord.get("recipe_name");
      // int prepTime = Integer.parseInt(csvRecord.get("prep_time"));
      // int cookingTime = Integer.parseInt(csvRecord.get("cook_time"));
      String ingredients = csvRecord.get("ingredients");
      // float rating = Float.parseFloat(csvRecord.get("rating"));
      String nutrition = csvRecord.get("nutrition");
      String[] nutrients = nutrition.split(",");
      Arrays.stream(nutrients).forEach(nutrient -> {
        String[] info = nutrient.split(" ");
        if (info.length == 2) {

        } else if (info.length == 3) {
        } else if (info.length == 4) {

        }
      });
      RecipeDTO recipeDTO = RecipeDTO.builder()
          .id(id)
          .recipeName(recipeName)
          // .prepTime(prepTime)
          // .cookingTime(cookingTime)
          .ingredients(ingredients)
          .nutrition(nutrition)
          // .rating(rating)
          .build();
      this.insertRecord(recipeDTO);
      return recipeDTO;
    }).toList();

    // this.insertBulkRecords(recipeDTOs);

  }

  private void insertRecord(RecipeDTO recipe) {
    try {
      ElasticsearchClient esClient = this.elasticsearchConnection.getClient();

      IndexRequest<RecipeDTO> request = IndexRequest.of(i -> i
          .index("recipes")
          .id(recipe.getId().toString())
          .document(recipe)
          .refresh(Refresh.True));

      IndexResponse response = esClient.index(request);
      logger.info(response.result().jsonValue());

      logger.info("recipe: {}, Indexed with version : {}", recipe.getRecipeName(), response.version());

    } catch (Exception e) {
      logger.info("unable to insert record : {}", e.getMessage());
    }
  }

  private void insertBulkRecords(List<RecipeDTO> recipes) {
    try {
      logger.info("bulk insert started");
      ElasticsearchClient esClient = this.elasticsearchConnection.getClient();
      BulkRequest.Builder br = new BulkRequest.Builder();
      recipes.stream().forEach(
          r -> br.operations(op -> op.index(idx -> idx.index(r.getId().toString()).id("recipes").document(r))));
      BulkResponse bulkResponse = esClient.bulk(br.build());

      if (bulkResponse.errors()) {
        logger.error("Bulk had errors");
        for (BulkResponseItem item : bulkResponse.items()) {
          if (item.error() != null) {
            logger.error(item.error().reason());
          }
        }
      }
      logger.info("bulk insert completed");

    } catch (Exception e) {
      logger.info("unable to insert record : {}", e.getMessage());
    }
  }
}
