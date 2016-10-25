package com.cagst.swkroa.codevalue;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a CodeValue within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "codeSetUID",
    "codeValueUID",
    "display",
    "meaning",
    "active",
    "codeValueUpdateCount"
})
@JsonDeserialize(builder = AutoValue_CodeValue.Builder.class)
public abstract class CodeValue implements Serializable, Comparable<CodeValue> {

  // CodeValue meanings
  public static final String DOCUMENT_NEWSLETTER = "DOCUMENT_NEWSLETTER";
  public static final String DOCUMENT_RENEWAL    = "DOCUMENT_RENEWAL";

  /**
   * Gets the unique identifier for the {@link CodeSet} this CodeValue is associated with.
   *
   * @return A {@link long} that uniquely identifies the {@link CodeSet} this CodeValue is associated with.
   */
  @JsonProperty(value = "codeSetUID", required = true)
  public abstract long getCodeSetUID();

  /**
   * Gets the unique identifier for the CodeValue.
   *
   * @return A {@link long} that uniquely identifies the CodeValue.
   */
  @JsonProperty(value = "codeValueUID", required = true)
  public abstract long getCodeValueUID();

  /**
   * @return The display / name of the CodeValue.
   */
  @JsonProperty(value = "display", required = true)
  public abstract String getDisplay();

  /**
   * Gets the meaning of the CodeValue.
   *
   * @return A {@link String} that represents the meaning of the CodeValue.
   */
  @Nullable
  @JsonProperty(value = "meaning")
  public abstract String getMeaning();

  /**
   * Gets the active status of the CodeValue.
   *
   * @return {@link boolean} <code>true</code> if the CodeValue is active, <code>false</code> otherwise.
   */
  @JsonProperty(value = "active")
  public abstract boolean isActive();

  /**
   * Gets the number of times this object has been updated.
   *
   * @return {@link long} number of times the object has been updated.
   */
  @JsonProperty(value = "codeValueUpdateCount")
  public abstract long getCodeValueUpdateCount();

  @Override
  public int hashCode() {
    return Objects.hash(getCodeSetUID(), getDisplay());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CodeValue)) {
      return false;
    }

    CodeValue rhs = (CodeValue) obj;

    return Objects.equals(getCodeSetUID(), rhs.getCodeSetUID()) &&
        Objects.equals(getDisplay(), rhs.getDisplay());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("display", getDisplay());
    builder.append("meaning", getMeaning());

    return builder.build();
  }

  @Override
  public int compareTo(CodeValue rhs) {
    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(getDisplay(), rhs.getDisplay());
    return builder.build();
  }

  /**
   * Returns a new {@link Builder} with default values.
   *
   * @return A new {@link Builder}.
   */
  public static Builder builder() {
    return new AutoValue_CodeValue.Builder()
        .setCodeSetUID(0L)
        .setCodeValueUID(0L)
        .setActive(true)
        .setCodeValueUpdateCount(0L);
  }

  /**
   * Returns a new {@link Builder} based upon the values from the specified {@link CodeValue}.
   *
   * @param codeValue
   *    The {@link CodeValue} to base this builder off of.
   *
   * @return A new {@link Builder}.
   */
  public static Builder builder(CodeValue codeValue) {
    return new AutoValue_CodeValue.Builder()
        .setCodeSetUID(codeValue.getCodeSetUID())
        .setCodeValueUID(codeValue.getCodeValueUID())
        .setDisplay(codeValue.getDisplay())
        .setMeaning(codeValue.getMeaning())
        .setActive(codeValue.isActive())
        .setCodeValueUpdateCount(codeValue.getCodeValueUpdateCount());
  }

  @AutoValue.Builder
  public interface Builder {
    /**
     * Sets the unique identifier for the {@link CodeSet} this CodeValue should be associated with.
     *
     * @param codeSetId
     *     A {@link long} that uniquely identifies the {@link CodeSet} this CodeValue should be associated with.
     */
    @JsonProperty(value = "codeSetUID", required = true)
    Builder setCodeSetUID(long codeSetId);

    /**
     * Sets the unique identifier for the CodeValue.
     *
     * @param codeValueUID
     *     A {@link long} that uniquely identifies the CodeValue.
     */
    @JsonProperty(value = "codeValueUID", required = true)
    Builder setCodeValueUID(long codeValueUID);

    /**
     * Sets the display / name of the CodeValue.
     *
     * @param display
     *     The {@link String} display / name for the CodeValue.
     */
    @JsonProperty(value = "display", required = true)
    Builder setDisplay(String display);

    /**
     * Sets the meaning of the CodeValue.
     *
     * @param meaning
     *     A {@link String} that represents the meaning of the CodeValue.
     */
    @JsonProperty(value = "meaning")
    Builder setMeaning(String meaning);

    /**
     * Sets the active status of the CodeValue.
     *
     * @param active
     *     {@link boolean} <code>true</code> to make the CodeValue active, <code>false</code> to make the object
     *     inactive.
     */
    @JsonProperty(value = "active")
    Builder setActive(boolean active);

    /**
     * Sets the number of times this object has been updated.
     *
     * @param updateCount
     *     {@link long} the number of times the object has been updated.
     */
    @JsonProperty(value = "codeValueUpdateCount")
    Builder setCodeValueUpdateCount(long updateCount);

    CodeValue build();
  }
}
