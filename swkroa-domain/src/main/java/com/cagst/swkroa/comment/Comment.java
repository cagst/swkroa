package com.cagst.swkroa.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;

import com.cagst.common.util.CGTCollatorBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Represents a Comment within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class Comment implements Serializable, Comparable<Comment> {
  private static final long serialVersionUID = -4070620060954592537L;

  public static final String MEMBERSHIP = "MEMBERSHIP";

  private long comment_id;
  private long parent_entity_id;
  private String parent_entity_name;
  private DateTime comment_dt;
  private String comment_txt;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getCommentUID() {
    return comment_id;
  }

  /* package */void setCommentUID(final long uid) {
    comment_id = uid;
  }

  public long getParentEntityUID() {
    return parent_entity_id;
  }

  public void setParentEntityUID(final long uid) {
    this.parent_entity_id = uid;
  }

  public String getParentEntityName() {
    return parent_entity_name;
  }

  public void setParentEntityName(final String name) {
    this.parent_entity_name = name;
  }

  @NotNull
  @Past
  public DateTime getCommentDate() {
    return comment_dt;
  }

  public void setCommentDate(final DateTime dt) {
    this.comment_dt = dt;
  }

  @NotNull
  @Size(min = 1, max = 1000)
  public String getCommentText() {
    return comment_txt;
  }

  public void setCommentText(final String comment) {
    this.comment_txt = comment;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getCommentUpdateCount() {
    return updt_cnt;
  }

  /* package */void setCommentUpdateCount(final long count) {
    this.updt_cnt = count;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(comment_dt);
    builder.append(comment_txt);

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Comment)) {
      return false;
    }

    Comment rhs = (Comment) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(comment_dt, rhs.getCommentDate());
    builder.append(comment_txt, rhs.getCommentText());

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("comment_dt", comment_dt);
    builder.append("comment_txt", comment_txt);

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Comment rhs) {
    if (rhs == null) {
      return 0;
    }

    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(comment_dt, rhs.getCommentDate());

    return builder.build();
  }
}
