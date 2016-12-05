package com.cagst.swkroa.codevalue;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.text.Collator;
import java.util.Objects;

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
  private static final long serialVersionUID = 5527727350150906492L;

  // CodeValue meanings
  public static final String DOCUMENT_NEWSLETTER = "DOCUMENT_NEWSLETTER";
  public static final String DOCUMENT_RENEWAL    = "DOCUMENT_RENEWAL";

  @JsonProperty(value = "codeSetUID", required = true)
  public abstract long getCodeSetUID();

  @JsonProperty(value = "codeValueUID", required = true)
  public abstract long getCodeValueUID();

  @JsonProperty(value = "display", required = true)
  public abstract String getDisplay();

  @Nullable
  @JsonProperty(value = "meaning")
  public abstract String getMeaning();

  @JsonProperty(value = "active")
  public abstract boolean isActive();

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
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(getDisplay(), rhs.getDisplay());
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
    @JsonProperty(value = "codeSetUID", required = true)
    Builder setCodeSetUID(long codeSetId);

    @JsonProperty(value = "codeValueUID", required = true)
    Builder setCodeValueUID(long codeValueUID);

    @JsonProperty(value = "display", required = true)
    Builder setDisplay(String display);

    @JsonProperty(value = "meaning")
    Builder setMeaning(String meaning);

    @JsonProperty(value = "active")
    Builder setActive(boolean active);

    @JsonProperty(value = "codeValueUpdateCount")
    Builder setCodeValueUpdateCount(long updateCount);

    CodeValue build();
  }
}
