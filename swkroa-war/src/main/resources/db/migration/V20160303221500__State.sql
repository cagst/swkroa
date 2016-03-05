CREATE TABLE country (
  country_code  CHAR(2) NOT NULL,
  country_name  VARCHAR(50) NOT NULL,
  active_ind    BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm  DATETIME NOT NULL,
  create_id     BIGINT NOT NULL,
  updt_dt_tm    DATETIME NOT NULL,
  updt_id       BIGINT NOT NULL,
  updt_cnt      INT DEFAULT 0 NOT NULL,
  CONSTRAINT country_pk PRIMARY KEY (country_code)
) ENGINE = InnoDB;

INSERT INTO country (country_code, country_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'United States', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO country (country_code, country_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'Canada', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

CREATE TABLE state (
  country_code  CHAR(2) NOT NULL,
  state_code    CHAR(2) NOT NULL,
  state_fips    CHAR(2) NULL,
  state_name    VARCHAR(50) NOT NULL,
  active_ind    BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm  DATETIME NOT NULL,
  create_id     BIGINT NOT NULL,
  updt_dt_tm    DATETIME NOT NULL,
  updt_id       BIGINT NOT NULL,
  updt_cnt      INT DEFAULT 0 NOT NULL,
  CONSTRAINT state_pk PRIMARY KEY (country_code, state_code),
  CONSTRAINT state_country_fk FOREIGN KEY (country_code) REFERENCES country (country_code)
) ENGINE = InnoDB;

INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'AL', '01', 'Alabama', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'AK', '02', 'Alaska', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'AS', '60', 'American Samoa', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'AZ', '04', 'Arizona', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'AR', '05', 'Arkansas', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'CA', '06', 'California', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'CO', '08', 'Colorado', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'CT', '09', 'Connecticut', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'DE', '10', 'Delaware', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'DC', '11', 'District of Columbia', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'FM', '64', 'Federated States Of Micronesia', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'FL', '12', 'Florida', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'GA', '13', 'Georgia', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'GU', '14', 'Guam', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'HI', '15', 'Hawaii', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'ID', '16', 'Idaho', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'IL', '17', 'Illinois', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'IN', '18', 'Indiana', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'IA', '19', 'Iowa', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'KS', '20', 'Kansas', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'KY', '21', 'Kentucky', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'LA', '22', 'Louisiana', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'ME', '23', 'Maine', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MH', '68', 'Marshall Islands', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MD', '24', 'Maryland', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MA', '25', 'Massachusetts', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MI', '26', 'Michigan', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MN', '27', 'Minnesota', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MS', '28', 'Mississippi', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MO', '29', 'Missouri', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MT', '30', 'Montana', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'NE', '31', 'Nebraska', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'NV', '32', 'Nevada', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'NH', '33', 'New Hampshire', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'NJ', '34', 'New Jersey', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'NM', '35', 'New Mexico', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'NY', '36', 'New York', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'NC', '37', 'North Carolina', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'ND', '38', 'North Dakota', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'MP', '69', 'Northern Mariana Islands', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'OH', '39', 'Ohio', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'OK', '40', 'Oklahoma', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'OR', '41', 'Oregon', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'PW', '70', 'Palau', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'PA', '42', 'Pennsylvania', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'PR', '72', 'Puerto Rico', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'RI', '44', 'Rhode Island', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'SC', '45', 'South Carolina', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'SD', '46', 'South Dakota', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'TN', '47', 'Tennessee', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'TX', '48', 'Texas', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'UT', '49', 'Utah', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'VT', '50', 'Vermont', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'VI', '78', 'Virgin Islands', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'VA', '51', 'Virginia', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'WA', '53', 'Washington', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'WV', '54', 'West Virginia', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'WI', '55', 'Wisconsin', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_fips, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('US', 'WY', '56', 'Wyoming', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'ON', 'Ontario', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'QC', 'Quebec', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'NS', 'Nova Scotia', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'NB', 'New Brunswick', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'MB', 'Manitoba', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'BC', 'British Columbia', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'PE', 'Prince Edward Island', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'SK', 'Saskatchewan', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'AB', 'Alberta', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO state (country_code, state_code, state_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES ('CA', 'NL', 'Newfoundland and Labrador', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
