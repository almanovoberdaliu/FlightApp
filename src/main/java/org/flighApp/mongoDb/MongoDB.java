package org.flighApp.mongoDb;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class MongoDB {
  private final ReactiveMongoClient reactiveMongoClient;

  @Inject
  public MongoDB(io.quarkus.mongodb.reactive.ReactiveMongoClient reactiveMongoClient) {
    this.reactiveMongoClient = reactiveMongoClient;
  }

  public <TEntity> ReactiveMongoCollection<TEntity> getCollection(
      String collectionName, Class<TEntity> entity) {
    return reactiveMongoClient.getDatabase("FlightAppDb").getCollection(collectionName, entity);
  }
}
