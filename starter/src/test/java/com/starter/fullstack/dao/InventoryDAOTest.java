package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test Inventory DAO.
 */
@DataMongoTest
@RunWith(SpringRunner.class)
public class InventoryDAOTest {
  @ClassRule
  public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  @Resource
  private MongoTemplate mongoTemplate;
  private InventoryDAO inventoryDAO;
  private static final String NAME = "Amber";
  private static final String PRODUCT_TYPE = "hops";

  @Before
  public void setup() {
    this.inventoryDAO = new InventoryDAO(this.mongoTemplate);
  }

  @After
  public void tearDown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test Find All method.
   */
  @Test
  public void findAll() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.mongoTemplate.save(inventory);
    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Assert.assertFalse(actualInventory.isEmpty());
  }

  /*
   * Test Function for create(Inventory inventory)
   * This function will create a new inventory, assert that the Mongo ID is null, 
   * and assert that the inventory created was added to the collection
   */
  @Test
  public void createTest() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setId("testID");
    Inventory addedInventory = inventoryDAO.create(inventory);
    List<Inventory> actualInventory = inventoryDAO.findAll();
    Assert.assertFalse(actualInventory.isEmpty());
    Assert.assertFalse(inventoryDAO.retrieve("testID").isPresent());
    Assert.assertNotNull(addedInventory);
    Query query = new Query(Criteria.where("id").is(addedInventory.getId()));
    Assert.assertEquals(mongoTemplate.findOne(query, Inventory.class), addedInventory);
  }

  @Test
  public void retrieveTest() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setId("testretrieveID");
    Inventory addedInventory = mongoTemplate.save(inventory);
    Optional<Inventory> retrievedInventory = inventoryDAO.retrieve(addedInventory.getId());
    Assert.assertTrue(retrievedInventory.isPresent());
    Assert.assertEquals(retrievedInventory.get(), addedInventory);

  }


  @Test
  public void deleteTest() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setId("testDeleteID");
    Inventory addedInventory = mongoTemplate.save(inventory);
    Assert.assertTrue(inventoryDAO.delete(addedInventory.getId()).isPresent());
    Query query = new Query(Criteria.where("id").is(addedInventory.getId()));
    Assert.assertNull(mongoTemplate.findOne(query, Inventory.class));
  }
}
