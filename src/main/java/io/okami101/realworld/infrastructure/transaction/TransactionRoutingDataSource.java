package io.okami101.realworld.infrastructure.transaction;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionRoutingDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
        ? DataSourceType.READ_ONLY
        : DataSourceType.READ_WRITE;
  }
}
