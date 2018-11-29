package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.po.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {
	
	 public Customer findByDevcMac(String devcMac);

}
