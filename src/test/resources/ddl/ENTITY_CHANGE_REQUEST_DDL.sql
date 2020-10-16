CREATE 	TABLE 	ENTITY_CHANGE_REQUEST (
  FK_USER_REQUEST_ID			        NUMBER(15)		            NOT NULL,		 
  CREATE_TS			        	        TIMESTAMP		              NOT NULL,
  LAST_MODIFIED_TS 		  	        	TIMESTAMP		              NOT NULL,
  APPLICATION_ID			            VARCHAR2(17)		          NOT NULL,
  PROPOSED_BUSINESS_ENTITY_CT			VARCHAR(20)	                  NOT NULL,
  CURRENT_BUSINESS_ENTITY_CD	  		VARCHAR(20)	                  NOT NULL,
  CREATE_USER_ID		              	VARCHAR2(20)		          NOT NULL,
  LAST_MODIFIED_USER_ID	          		VARCHAR(20)		            NOT NULL
    );

ALTER 	TABLE 	ENTITY_CHANGE_REQUEST
ADD CONSTRAINT fk_entity_user_req_id
FOREIGN KEY (FK_USER_REQUEST_ID)
REFERENCES USER_REQUEST(USER_REQUEST_ID);
