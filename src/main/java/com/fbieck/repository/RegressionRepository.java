package com.fbieck.repository;

import com.fbieck.entities.Regression;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegressionRepository extends CrudRepository<Regression, String> {
}
