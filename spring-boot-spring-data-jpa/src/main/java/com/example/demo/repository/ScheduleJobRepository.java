package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ScheduleJob;

@Repository
public interface ScheduleJobRepository extends JpaRepository<ScheduleJob, Long> , JpaSpecificationExecutor<ScheduleJob> {

}