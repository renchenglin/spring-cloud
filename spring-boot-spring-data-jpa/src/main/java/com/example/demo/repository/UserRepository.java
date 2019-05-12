package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
//	@Query(value = "SELECT * FROM t_user WHERE :sql",
//    	countQuery = "SELECT count(id) FROM t_user WHERE :sql",
//    	nativeQuery = true)
	
	@Query(value = "select * from t_user where :sql",
			countQuery = "select count(id) from t_user where :sql",
			nativeQuery = true)
	Page<User> findUserBySql(@Param("sql") String sql, Pageable pageable);
}