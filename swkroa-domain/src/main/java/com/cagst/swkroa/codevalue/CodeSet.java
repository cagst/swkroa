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
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "codeSetUID", required = true)
  public abstract long getCodeSetUID();

  @JsonProperty(value = "display", required = true)
  public abstract String getDisplay();

  @JsonProperty(value = "meaning", required = true)
  public abstract String getMeaning();

  @JsonProperty(value = "active")
  public abstract boolean isActive();

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
  public interface Builder {
    @JsonProperty(value = "codeSetUID", required = true)
    Builder setCodeSetUID(long uid);

    @JsonProperty(value = "display", required = true)
    Builder setDisplay(String display);

    @JsonProperty(value = "meaning", required = true)
    Builder setMeaning(String meaning);

    @JsonProperty(value = "active")
    Builder setActive(boolean active);

    @JsonProperty(value = "codeSetUpdateCount")
    Builder setCodeSetUpdateCount(long updateCount);

    CodeSet build();
  }
}
