package com.cagst.swkroa.codevalue;

import java.io.Serializable;
import java.text.Collator;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a CodeSet within the system.
 *
 * @author Craig Gaskill
 */
public final class CodeSet implements Serializable, Comparable<CodeSet> {
  private long codeset_id;
  private String codeset_display;
  private String codeset_meaning;
  private boolean active_ind = true;
  private long updt_cnt;

  /**
   * Gets the unique identifier for the CodeSet;
   *
   * @return A {@link long} that uniquely identifies the CodeSet.
   */
  public long getCodeSetUID() {
    return codeset_id;
  }

  /**
   * Sets the unique identifier for the CodeSet.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the CodeSet.
   */
  /* package */void setCodeSetUID(final long uid) {
    this.codeset_id = uid;
  }

  /**
   * Gets the display / name of the CodeSet.
   *
   * @return The {@link String} display / name of the CodeSet.
   */
  public String getDisplay() {
    return codeset_display;
  }

  /**
   * Sets the display / name of the CodeSet.
   *
   * @param display
   *     The {@link String} display / name for the CodeSet.
   */
  public void setDisplay(final String display) {
    this.codeset_display = display;
  }

  /**
   * Gets the meaning of the CodeSet.
   *
   * @return A {@link String} that represents the meaning of the CodeSet.
   */
  public String getMeaning() {
    return codeset_meaning;
  }

  /**
   * Sets the meaning for the CodeSet.
   *
   * @param meaning
   *     A {@link String} that represents the meaning of the CodeSet.
   */
  public void setMeaning(final String meaning) {
    this.codeset_meaning = meaning;
  }

  /**
   * Gets the active status of the CodeSet.
   *
   * @return {@link boolean} <code>true</code> if the CodeSet is active, <code>false</code>
   * otherwise.
   */
  public boolean isActive() {
    return active_ind;
  }

  /**
   * Sets the active status of the CodeSet.
   *
   * @param active
   *     {@link boolean} <code>true</code> to make the CodeSet active, <code>false</code> to
   *     make the object inactive.
   */
  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  /**
   * Gets the number of times this object has been updated.
   *
   * @return {@link long} number of times the object has been updated.
   */
  public long getCodeSetUpdateCount() {
    return updt_cnt;
  }

  /**
   * Sets the number of times this object has been updated.
   *
   * @param updateCount
   *     {@link long} the number of times the object has been updated.
   */
	/* package */void setCodeSetUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  @Override
  public int hashCode() {
    return codeset_display.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CodeSet)) {
      return false;
    }

    CodeSet rhs = (CodeSet) obj;

    return codeset_display.equals(rhs.getDisplay());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("display", codeset_display);
    builder.append("meaning", codeset_meaning);

    return builder.build();
  }

  @Override
  public int compareTo(final CodeSet rhs) {
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(codeset_display, rhs.getDisplay());
  }
}
