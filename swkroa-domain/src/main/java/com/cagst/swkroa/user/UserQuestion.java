package com.cagst.swkroa.user;

import java.io.Serializable;
import java.util.Objects;

import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a User's response to a Security Question.
 *
 * @author Craig Gaskill
 */
@AutoValue
public abstract class UserQuestion implements Serializable {
  public abstract long getUserQuestionUID();

  public abstract long getQuestionCD();

  public abstract String getAnswer();

  public abstract boolean isActive();

  public abstract long getUserQuestionUpdateCount();

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("question", getQuestionCD());
    builder.append("answer", getAnswer());

    return builder.build();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getQuestionCD(), getAnswer());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof UserQuestion)) {
      return false;
    }

    UserQuestion rhs = (UserQuestion) obj;

    return Objects.equals(getQuestionCD(), rhs.getQuestionCD()) &&
        Objects.equals(getAnswer(), rhs.getAnswer());
  }

  public static Builder builder() {
    return new AutoValue_UserQuestion.Builder()
        .setUserQuestionUID(0L)
        .setActive(true)
        .setUserQuestionUpdateCount(0L);
  }

  @AutoValue.Builder
  public interface Builder {
    Builder setUserQuestionUID(long uid);

    Builder setQuestionCD(long questionCD);

    Builder setAnswer(String answer);

    Builder setActive(boolean active);

    Builder setUserQuestionUpdateCount(long updateCount);

    UserQuestion build();
  }
}
