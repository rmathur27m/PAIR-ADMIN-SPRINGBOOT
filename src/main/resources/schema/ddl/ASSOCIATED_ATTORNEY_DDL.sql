CREATE 	TABLE 	ASSOCIATED_ATTORNEY (
 ASSOCIATED_ATTORNEY_ID			    	NUMBER(15)					PRIMARY KEY,
 FK_CUSTOMER_NUMBER_REQUEST_ID  		NUMBER(15)	               NOT NULL,
 REGISTRATION_NO				        CHAR(6)		            	NOT NULL, 
 GIVEN_NM						        VARCHAR2(50),
 FAMILY_NM						        VARCHAR2(50),
 MIDDLE_NM						        VARCHAR2(50),
 NAME_SUFFIX_TX					        VARCHAR2(50),
 ASSOCIATED_PURM_REG_NO_IN				CHAR(1)
  );


ALTER 	TABLE 	ASSOCIATED_ATTORNEY
ADD CONSTRAINT RI_CNR_AA
FOREIGN KEY (FK_CUSTOMER_NUMBER_REQUEST_ID)
REFERENCES CUSTOMER_NUMBER_REQUEST(CUSTOMER_NUMBER_REQUEST_ID);

CREATE SEQUENCE PK_ASSOCIATED_ATTORNEY_SEQ
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE; 
/
CREATE OR REPLACE TRIGGER 	PK_ASSOCIATED_ATTORNEY_TRIGGER
  BEFORE INSERT ON ASSOCIATED_ATTORNEY
 FOR EACH ROW
BEGIN
  SELECT PK_ASSOCIATED_ATTORNEY_SEQ.nextval
    INTO :new.ASSOCIATED_ATTORNEY_ID
    FROM dual;
END;
/


ALTER TABLE ASSOCIATED_ATTORNEY
ADD CONSTRAINT  CK_ASSOCIATED_ATTORNEY_PURM
  CHECK (ASSOCIATED_PURM_REG_NO_IN   IN ('Y', 'N'));
  
COMMENT ON TABLE ASSOCIATED_ATTORNEY IS 'Table to store the Attorney or Agent  which will be assoicated with a customer number from CUSTOMER_NUMBER_REQUEST.Each Attorney or Agent must have a registration number.   This is saved as draft in PAIR database until customer submits to make final changes in PALM database.  Once the final submission is made to PALM the records are used as audit logs for any future customer queries.';

COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.FK_CUSTOMER_NUMBER_REQUEST_ID	IS 'Foreign key mapped to CUSTOMER_NUMBER_REQUEST table '; 
COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.FK_USER_ID	IS 'Primary Key '; 
COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.REGISTRATION_NO	IS 'Attorney Registration Number entered from new CN Request';  
COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.GIVEN_NM	IS 'Attorney Given name ';
COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.FAMILY_NM	IS 'Attorney Family name ';
COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.MIDDLE_NM	IS 'Attorney Middle name ';
COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.NAME_SUFFIX_TX	IS 'Attorney Name Suffix'; 
COMMENT ON COLUMN 	ASSOCIATED_ATTORNEY.ASSOCIATED_PURM_REG_NO_IN	IS 'Indiactor whether Reg Number is associated with New Customer Number or Not'; 
