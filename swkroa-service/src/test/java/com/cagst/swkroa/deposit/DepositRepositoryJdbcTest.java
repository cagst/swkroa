package com.cagst.swkroa.deposit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.cagst.swkroa.test.BaseTestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Test class for the {@link DepositRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class DepositRepositoryJdbcTest extends BaseTestRepository {
  private DepositRepositoryJdbc repo;

  @Before
  public void setUp() {
    repo = new DepositRepositoryJdbc(createTestDataSource());
  }

  /**
   * Test the getDeposits method.
   */
  @Test
  public void testGetDeposits() {
    List<Deposit> deposits = repo.getDeposits();

    assertNotNull("Ensure the deposits is not null.", deposits);
    assertEquals("Ensure we have the correct number of Deposits.", 2, deposits.size());
  }

  /**
   * Test the getDeposit method and not finding any deposit.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetDeposit_NotFound() {
    repo.getDeposit(999L);
  }

  /**
   * Test the getDeposit method and finding a deposit.
   */
  @Test
  public void testGetDeposit_Found() {
    Deposit deposit = repo.getDeposit(1L);

    assertNotNull("Ensure the deposit is not null.", deposit);
    assertEquals("Ensure we found the correct deposit.", "REF1", deposit.getDepositNumber());
  }
}
