package com.cagst.swkroa.system;

import javax.annotation.Nullable;
import java.io.Serializable;

import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a System Setting defined within the system.
 */
@AutoValue
@JsonPropertyOrder({
    "systemKey",
    "systemName",
    "systemValue"
})
@JsonDeserialize
public abstract class SystemSetting implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "systemKey", required = true)
  public abstract String getSystemKey();

  @JsonProperty(value = "systemName", required = true)
  public abstract String getSystemName();

  @Nullable
  @JsonProperty(value = "systemValue")
  public abstract String getSystemValue();

  @Override
  public int hashCode() {
    return getSystemName().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SystemSetting)) {
      return false;
    }

    return getSystemKey().equals(((SystemSetting)obj).getSystemKey());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE)
        .append("key", getSystemKey())
        .append("name", getSystemName())
        .append("value", getSystemValue())
        .build();
  }

  /**
   * Returns a new {@link Builder} with default values.
   *
   * @return A new {@link Builder}.
   */
  public static Builder builder() {
    return new AutoValue_SystemSetting.Builder();
  }

  @AutoValue.Builder
  public interface Builder {
    @JsonProperty(value = "systemKey", required = true)
    Builder setSystemKey(String key);

    @JsonProperty(value = "systemName", required = true)
    Builder setSystemName(String name);

    @JsonProperty(value = "systemValue")
    Builder setSystemValue(String value);

    SystemSetting build();
  }
}
