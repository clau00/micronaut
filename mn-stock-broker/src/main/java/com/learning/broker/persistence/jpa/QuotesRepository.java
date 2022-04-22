package com.learning.broker.persistence.jpa;

import com.learning.broker.persistence.model.QuoteDTO;
import com.learning.broker.persistence.model.QuoteEntity;
import com.learning.broker.persistence.model.SymbolEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Integer> {

    @Override
    List<QuoteEntity> findAll();

    Optional<QuoteEntity> findBySymbol(SymbolEntity symbol);

    // Ordering
    List<QuoteDTO> listOrderByVolumeDesc();

    List<QuoteDTO> listOrderByVolumeAsc();

    // Filtering
    List<QuoteDTO> findByVolumeGreaterThan(BigDecimal volume);
    List<QuoteDTO> findByVolumeGreaterThanOrderByVolumeAsc(BigDecimal volume);
}
