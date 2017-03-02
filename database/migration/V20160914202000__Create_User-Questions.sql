CREATE TABLE user_question (
  user_question_id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id                BIGINT UNSIGNED NOT NULL,
  question_cd            BIGINT UNSIGNED NOT NULL,
  answer                 CHAR(60) NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT NOT NULL,
  updt_cnt               INT DEFAULT 0 NOT NULL,
  CONSTRAINT user_question_pk PRIMARY KEY (user_question_id),
  CONSTRAINT user_question_fk1 FOREIGN KEY (user_id) REFERENCES user (user_id)
) ENGINE = InnoDb;
