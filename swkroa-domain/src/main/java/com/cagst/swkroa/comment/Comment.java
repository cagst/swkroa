package com.cagst.swkroa.comment;

import java.io.Serializable;
import java.util.Objects;

import com.cagst.common.util.CGTCollatorBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Represents a Comment within the system.
 *
 * @author Craig Gaskill
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

  public DateTime getCommentDate() {
    return comment_dt;
  }

  public void setCommentDate(final DateTime dt) {
    this.comment_dt = dt;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(comment_dt, comment_txt);
  }

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

    return Objects.equals(comment_dt, rhs.getCommentDate()) &&
        Objects.equals(comment_txt, rhs.getCommentText());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("comment_dt", comment_dt);
    builder.append("comment_txt", comment_txt);

    return builder.build();
  }

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
