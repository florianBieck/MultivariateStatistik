package com.fbieck.repository;

import com.fbieck.entities.RegressionCoefficient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegressionCoefficientRepository extends CrudRepository<RegressionCoefficient, Integer> {
}
