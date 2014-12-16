package com.cagst.swkroa.deposit;

import javax.sql.DataSource;

import com.cagst.common.db.BaseRepositoryJdbc;

/**
 * Created by cgaskill on 7/5/14.
 */
public final class DepositRepositoryJdbc extends BaseRepositoryJdbc implements DepositRepository {
  public DepositRepositoryJdbc(DataSource dataSource) {
    super(dataSource);
  }
}
