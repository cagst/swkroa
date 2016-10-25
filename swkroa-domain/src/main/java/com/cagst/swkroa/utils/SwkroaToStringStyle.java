package com.cagst.swkroa.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Custom formatting for the
 */
public class SwkroaToStringStyle extends ToStringStyle {
  public static final ToStringStyle SWKROA_PREFIX_STYLE = new SwkroaToStringStyle();

  private static final String AUTO_VALUE = "AutoValue_";

  private SwkroaToStringStyle() {
    super();
    this.setUseIdentityHashCode(false);
  }

  @Override
  protected void appendClassName(final StringBuffer buffer, final Object object) {
    if (isUseClassName() && object != null) {
      String className = object.getClass().getSimpleName();
      if (StringUtils.startsWith(className, AUTO_VALUE)) {
        className = StringUtils.replace(className, AUTO_VALUE, StringUtils.EMPTY);
      }

      buffer.append(className);
    }
  }
}
