package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.naming.Name;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

/**
 * Inventory DAO
 */
public class InventoryDAO {
  private final MongoTemplate mongoTemplate;
  private static final String NAME = "name";
  private static final String PRODUCT_TYPE = "productType";

  private static final String ID = "id";

  private static final String DESCRIPTION = "description";

  private static final String AVERAGE_PRICE = "averagePrice";

  private static final String AMOUNT = "amount";

  private static final String UNIT_OF_MEASUREMENT = "unitOfMeasurement";

  private static final String BEST_BEFORE_DATE = "bestBeforeDate";

  private static final String NEVER_EXPIRES = "neverExpires";

  private static final String AVAILABLE_STORES = "availableStores";

  /**
   * Default Constructor.
   * @param mongoTemplate MongoTemplate.
   */
  public InventoryDAO(MongoTemplate mongoTemplate) {
    Assert.notNull(mongoTemplate, "MongoTemplate must not be null.");
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * Constructor to build indexes for rate blackout object
   */
  @PostConstruct
  public void setupIndexes() {
    IndexOperations indexOps = this.mongoTemplate.indexOps(Inventory.class);
    indexOps.ensureIndex(new Index(NAME, Sort.Direction.ASC));
    indexOps.ensureIndex(new Index(PRODUCT_TYPE, Sort.Direction.ASC));
  }

  /**
   * Find All Inventory.
   * @return List of found Inventory.
   */
  public List<Inventory> findAll() {
    return this.mongoTemplate.findAll(Inventory.class);
  }

  /**
   * Save Inventory.
   * @param inventory Inventory to Save/Update.
   * @return Created/Updated Inventory.
   */
  public Inventory create(Inventory inventory) {
    inventory.setId(null);
    return mongoTemplate.insert(inventory);
  }

  /**
   * Retrieve Inventory.
   * @param id Inventory id to Retrieve.
   * @return Found Inventory.
   */
  public Optional<Inventory> retrieve(String id) {
    Query query = new Query(Criteria.where(ID).is(id));
    return Optional.ofNullable(mongoTemplate.findOne(query, Inventory.class));
  }

  /**
   * Update Inventory.
   * @param id Inventory id to Update.
   * @param inventory Inventory to Update.
   * @return Updated Inventory.
   */
  public Optional<Inventory> update(String id, Inventory inventory) {
    Query query = new Query(Criteria.where(ID).is(id));
    Update update = new Update();
    update.set(NAME, inventory.getName());
    update.set(PRODUCT_TYPE, inventory.getProductType());
    update.set(DESCRIPTION, inventory.getDescription());
    update.set(AVERAGE_PRICE, inventory.getAveragePrice());
    update.set(AMOUNT, inventory.getAmount());
    update.set(UNIT_OF_MEASUREMENT, inventory.getUnitOfMeasurement());
    update.set(BEST_BEFORE_DATE, inventory.getBestBeforeDate());
    update.set(NEVER_EXPIRES, inventory.isNeverExpires());
    update.set(AVAILABLE_STORES, inventory.getAvailableStores());
    return Optional.ofNullable(mongoTemplate.findAndModify(query, update, Inventory.class));
  }

  /**
   * Delete Inventory By Id.
   * @param id id of Inventory.
   * @return Deleted Inventory.
   */
  public Optional<Inventory> delete(String id) {

    Query query = new Query(Criteria.where(ID).is(id));
    return Optional.ofNullable(mongoTemplate.findAndRemove(query, Inventory.class));
  }
}
