package com.fbieck.repository;

import com.fbieck.entities.SymbolRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRelationRepository extends CrudRepository<SymbolRelation, Integer> {

    SymbolRelation findBySymbol(String symbol);

    SymbolRelation findByUserid(Long userid);
}
