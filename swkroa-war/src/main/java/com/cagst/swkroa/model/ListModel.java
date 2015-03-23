package com.cagst.swkroa.model;

import java.util.List;

/**
 * A model that contains information contained in a list with a total count of all possible items.
 *
 * @author Craig Gaskill
 */
public class ListModel<T> {
  private List<T> items;
  private long totalCount;

  public ListModel(final List<T> items, final long totalCount) {
    this.items = items;
    this.totalCount = totalCount;
  }

  public List<T> getItems() {
    return items;
  }

  public long getTotalItemCount() {
    return totalCount;
  }
}
