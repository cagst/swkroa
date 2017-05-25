package com.cagst.swkroa.comment;

import java.io.Serializable;
import java.util.Objects;

import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents a Comment within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "commentUID",
    "parentEntityUID",
    "parentEntityName",
    "commentDate",
    "commentText",
    "active",
    "commentUpdateCount"
})
@JsonDeserialize(builder = Comment.Builder.class)
public abstract class Comment implements Serializable, Comparable<Comment> {
  private static final long serialVersionUID = 1L;

  public static final String MEMBERSHIP = "MEMBERSHIP";

  @JsonProperty(value = "commentUID")
  public abstract long getCommentUID();

  @JsonProperty(value = "parentEntityUID")
  public abstract long getParentEntityUID();

  @JsonProperty(value = "parentEntityName", required = true)
  public abstract String getParentEntityName();

  @JsonProperty(value = "commentDate", required = true)
  public abstract DateTime getCommentDate();

  @JsonProperty(value = "commentText", required = true)
  public abstract String getCommentText();

  @JsonProperty(value = "active")
  public abstract boolean isActive();

  @JsonProperty(value = "commentUpdateCount")
  public abstract long getCommentUpdateCount();

  @Override
  public int hashCode() {
    return Objects.hash(getCommentDate(), getCommentText());
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

    return Objects.equals(getCommentDate(), rhs.getCommentDate()) &&
        Objects.equals(getCommentText(), rhs.getCommentText());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("comment_dt", getCommentDate());
    builder.append("comment_txt", getCommentText());

    return builder.build();
  }

  @Override
  public int compareTo(final Comment rhs) {
    if (rhs == null) {
      return 0;
    }

    return getCommentDate().compareTo(rhs.getCommentDate());
  }

  /**
   * Returns a {@link Builder} with default values.
   *
   * @return A {@link Builder}
   */
  public static Builder builder() {
    return new AutoValue_Comment.Builder()
        .setCommentUID(0L)
        .setParentEntityUID(0L)
        .setActive(true)
        .setCommentUpdateCount(0L);
  }

  /**
   * Returns a {@link Builder} based upon the values from the specified {@link Comment}.
   *
   * @param comment
   *    The {@link Comment} to base this builder off of.
   *
   * @return A {@link Builder}.
   */
  public static Builder builder(Comment comment) {
    return new AutoValue_Comment.Builder()
        .setCommentUID(comment.getCommentUID())
        .setParentEntityUID(comment.getParentEntityUID())
        .setParentEntityName(comment.getParentEntityName())
        .setCommentDate(comment.getCommentDate())
        .setCommentText(comment.getCommentText())
        .setActive(comment.isActive())
        .setCommentUpdateCount(comment.getCommentUpdateCount());
  }

  @AutoValue.Builder
  @JsonPOJOBuilder
  public abstract static class Builder {
    @JsonProperty(value = "commentUID")
    public abstract Builder setCommentUID(long uid);

    @JsonProperty(value = "parentEntityUID")
    public abstract Builder setParentEntityUID(long entityUID);

    @JsonProperty(value = "parentEntityName", required = true)
    public abstract Builder setParentEntityName(String entityName);

    @JsonProperty(value = "commentDate", required = true)
    public abstract Builder setCommentDate(DateTime commentDate);

    @JsonProperty(value = "commentText", required = true)
    public abstract Builder setCommentText(String commentText);

    @JsonProperty(value = "active")
    public abstract Builder setActive(boolean active);

    @JsonProperty(value = "commentUpdateCount")
    public abstract Builder setCommentUpdateCount(long updateCount);

    public abstract Comment build();

    @JsonCreator
    private static Builder builder() {
      return Comment.builder();
    }
  }
}
