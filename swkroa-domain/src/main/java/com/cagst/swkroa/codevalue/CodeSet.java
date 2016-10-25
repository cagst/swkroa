package com.cagst.swkroa.codevalue;

import java.io.Serializable;
import java.text.Collator;

import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a CodeSet within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "codeSetUID",
    "display",
    "meaning",
    "active",
    "codeSetUpdateCount"
})
@JsonDeserialize(builder = AutoValue_CodeSet.Builder.class)
public abstract class CodeSet implements Serializable, Comparable<CodeSet> {
  /**
   * Gets the unique identifier for the CodeSet;
   *
   * @return A {@link long} that uniquely identifies the CodeSet.
   */
  @JsonProperty(value = "codeSetUID", required = true)
  public abstract long getCodeSetUID();

  /**
   * Gets the display / name of the CodeSet.
   *
   * @return The {@link String} display / name of the CodeSet.
   */
  @JsonProperty(value = "display", required = true)
  public abstract String getDisplay();

  /**
   * Gets the meaning of the CodeSet.
   *
   * @return A {@link String} that represents the meaning of the CodeSet.
   */
  @JsonProperty(value = "meaning", required = true)
  public abstract String getMeaning();

  /**
   * Gets the active status of the CodeSet.
   *
   * @return {@link boolean} <code>true</code> if the CodeSet is active, <code>false</code>
   * otherwise.
   */
  @JsonProperty(value = "active")
  public abstract boolean isActive();

  /**
   * Gets the number of times this object has been updated.
   *
   * @return {@link long} number of times the object has been updated.
   */
  @JsonProperty(value = "codeSetUpdateCount")
  public abstract long getCodeSetUpdateCount();

  @Override
  public int hashCode() {
    return getDisplay().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
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

    return getDisplay().equals(rhs.getDisplay());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("display", getDisplay());
    builder.append("meaning", getMeaning());

    return builder.build();
  }

  @Override
  public int compareTo(CodeSet rhs) {
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(getDisplay(), rhs.getDisplay());
  }

  public static Builder builder() {
    return new AutoValue_CodeSet.Builder()
        .setCodeSetUID(0L)
        .setActive(true)
        .setCodeSetUpdateCount(0L);
  }

  @AutoValue.Builder
  interface Builder {
    /**
     * Sets the unique identifier for the CodeSet.
     *
     * @param uid
     *     A {@link long} that uniquely identifies the CodeSet.
     */
    @JsonProperty(value = "codeSetUID", required = true)
    Builder setCodeSetUID(long uid);

    /**
     * Sets the display / name of the CodeSet.
     *
     * @param display
     *     The {@link String} display / name for the CodeSet.
     */
    @JsonProperty(value = "display", required = true)
    Builder setDisplay(String display);

    /**
     * Sets the meaning for the CodeSet.
     *
     * @param meaning
     *     A {@link String} that represents the meaning of the CodeSet.
     */
    @JsonProperty(value = "meaning", required = true)
    Builder setMeaning(String meaning);

    /**
     * Sets the active status of the CodeSet.
     *
     * @param active
     *     {@link boolean} <code>true</code> to make the CodeSet active, <code>false</code> to
     *     make the object inactive.
     */
    @JsonProperty(value = "active")
    Builder setActive(boolean active);

    /**
     * Sets the number of times this object has been updated.
     *
     * @param updateCount
     *     {@link long} the number of times the object has been updated.
     */
    @JsonProperty(value = "codeSetUpdateCount")
    Builder setCodeSetUpdateCount(long updateCount);

    CodeSet build();
  }
}
