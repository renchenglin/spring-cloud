package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@ConditionalOnProperty(prefix = "billing-engine.billing.system", name = "mongodb-transaction-control", havingValue = "true")
@EnableTransactionManagement(proxyTargetClass = true)
class MongoConfig {
   @Bean
   public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
       return new MongoTransactionManager(dbFactory);
   }
}