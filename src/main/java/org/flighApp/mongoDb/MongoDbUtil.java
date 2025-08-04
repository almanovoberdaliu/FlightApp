package org.flighApp.mongoDb;

import com.mongodb.client.model.UpdateOptions;
import io.quarkus.mongodb.FindOptions;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import java.util.List;
import org.bson.conversions.Bson;
import org.flighApp.Flight.models.PaginationParams;
import org.flighApp.utils.Utils;

public class MongoDbUtil {

  public static <T> Uni<List<T>> aggregate(
      ReactiveMongoCollection<T> collection, List<Bson> pipeline) {
    return collection.aggregate(pipeline).collect().asList();
  }

  public static <T, Z> Uni<List<Z>> aggregate(
      ReactiveMongoCollection<T> collection, List<Bson> pipeline, Class<Z> clazz) {
    return collection.aggregate(pipeline, clazz).collect().asList();
  }

  public static <T> Uni<List<T>> find(ReactiveMongoCollection<T> collection, Bson filter) {
    return collection.find(filter).collect().asList();
  }

  public static <T> Uni<T> findFirst(ReactiveMongoCollection<T> collection, Bson filter) {
    return collection.find(filter).collect().first();
  }

  public static <T> Uni<List<T>> find(ReactiveMongoCollection<T> collection) {
    return collection.find().collect().asList();
  }

  public static <T> Uni<List<T>> find(
      ReactiveMongoCollection<T> collection, PaginationParams paginationParams) {
    FindOptions options = paginationParams.toFindOptions();
    return collection.find(options).collect().asList();
  }

  public static <T> Uni<Boolean> exists(ReactiveMongoCollection<T> collection, Bson filter) {
    return collection
            .find(filter)
            .collect().asList()
            .map(list -> !list.isEmpty());
  }

  public static <T> Uni<Void> insertOne(ReactiveMongoCollection<T> collection, T document) {
    return collection.insertOne(document).replaceWithVoid();
  }

  public static <T> Uni<Void> insertMany(ReactiveMongoCollection<T> collection, List<T> documents) {
    return collection.insertMany(documents).replaceWithVoid();
  }

  public static <T> Uni<Void> deleteOne(ReactiveMongoCollection<T> collection, Bson filter) {
    return collection.deleteOne(filter).replaceWithVoid();
  }

  public static <T> Uni<Void> deleteMany(ReactiveMongoCollection<T> collection, Bson filter) {
    return collection.deleteMany(filter).replaceWithVoid();
  }

  public static <T> Uni<Void> replaceOne(
      ReactiveMongoCollection<T> collection, Bson filter, T document) {
    return collection.replaceOne(filter, document).replaceWithVoid();
  }

  public static <T> Uni<T> updateOne(
      ReactiveMongoCollection<T> collection, Bson filter, Bson update) {
    return collection
        .updateOne(filter, update)
        .flatMap(
            result -> {
              if (result.getModifiedCount() > 0) {
                return findFirst(collection, filter);
              } else {
                UpdateOptions options = new UpdateOptions().upsert(true);
                return collection
                    .updateOne(filter, update, options)
                    .flatMap(upsertResult -> findFirst(collection, filter));
              }
            });
  }

  public static <T> Uni<List<T>> updateMany(
      ReactiveMongoCollection<T> collection, Bson filter, Bson update) {
    return collection
        .updateMany(filter, update)
        .flatMap(
            result -> {
              if (result.getModifiedCount() > 0) {
                return find(collection, filter);
              } else {
                UpdateOptions options = new UpdateOptions().upsert(true);
                return collection
                    .updateMany(filter, update, options)
                    .flatMap(upsertResult -> find(collection, filter));
              }
            });
  }
}
