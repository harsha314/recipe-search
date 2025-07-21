package com.recipes.entity;

// import org.springframework.data.elasticsearch.annotations.Document;
// import org.springframework.data.elasticsearch.annotations.Field;
// import org.springframework.data.elasticsearch.annotations.FieldType;

// import jakarta.persistence.Id;
import lombok.Data;

// @Data
// @Document(indexName = "recipes")
public class Recipe {
  // @Id
  private Integer id;

  // @Field(type = FieldType.Text, name = "recipe_name")
  private String recipeName;

  // @Field(type = FieldType.Integer, name = "prep_time")
  private Integer prepTime;

  // @Field(type = FieldType.Integer, name = "cook_time")
  private Integer cookTime;

  // @Field(type = FieldType.Text, name = "ingredients")
  private String ingredients;

  // @Field(type = FieldType.Keyword, name = "rating")
  private String rating;
}