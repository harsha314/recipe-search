# Spring Boot Recipe Search Engine Project

## Introduction

Combining Elasticsearch with Spring Boot is a practical and common real-world use case. Spring Data Elasticsearch provides a great abstraction layer, making it easier to interact with Elasticsearch from a Spring application.

## Project Overview

Build a Spring Boot application that allows users to search, filter, and view recipes. This project covers:

- Core Spring Boot concepts (REST APIs, Spring Data)
- Essential Elasticsearch features (indexing, full-text search, filtering, aggregations)

## Why It's Good for Beginners

- **Relatable Domain**: Recipes are easily understood, making data and search requirements clear
- **Structured Data**: Well-defined fields (title, ingredients, instructions, cooking time, cuisine, diet tags)
- **Full-Text Search**: Practice searching across ingredients and instructions
- **Filtering/Faceting**: Natural implementation of Elasticsearch aggregations
- **Spring Data Elasticsearch**: Learn essential annotations (@Document, @Field)

## Core Features

### 1. Recipe Ingestion

- Spring Boot service for file-based recipe data import (JSON, CSV)
- REST endpoint for manual recipe addition

### 2. Search API

- REST endpoint: `/api/recipes/search`
- Full-text search capabilities across:
  - Recipe titles
  - Ingredients
  - Instructions
- Returns relevant recipe matches

### 3. Filtering/Faceting (Optional)

- Filter options:
  - Cuisine type
  - Dietary tags (e.g., "vegetarian", "gluten-free")
  - Cooking time range
- Aggregations with counts (e.g., "Italian (50)", "Vegan (25)")

### 4. Recipe Details

- REST endpoint: `/api/recipes/{id}`
- Single recipe retrieval by ID

## Technical Stack

### Required Components

- Java 17+
- Spring Boot 3.x
- Spring Data Elasticsearch
- Elasticsearch & Kibana (Docker recommended)
- Maven/Gradle

### Optional Tools

- Postman/Insomnia for API testing
- Frontend options:
  - Basic HTML/JavaScript
  - React/Angular/Vue
  <!--

## Implementation Guide

### 1. Project Setup

1. Visit [Spring Initializr](https://start.spring.io)
2. Add dependencies:
   - Spring Web
   - Spring Data Elasticsearch
   - Lombok (optional)

### 2. Elasticsearch Configuration

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
```

### 3. Entity Definition

Create Recipe.java with appropriate annotations:

- `@Document`
- `@Id -->
