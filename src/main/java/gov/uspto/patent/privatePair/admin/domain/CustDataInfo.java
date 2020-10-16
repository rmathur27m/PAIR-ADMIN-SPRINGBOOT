package gov.uspto.patent.privatePair.admin.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
	 * @author Hemnath Kunthu
	 * 
	 * Date: 11/07/2005 
	 * 
	 * CustDataInfo class is serializable value object. 
	 *  
	 */
@Data
public class CustDataInfo implements Serializable{
		
		String name_line_one_o;
		String name_line_two_o;
		String street_line_one_o;
		String street_line_two_o;    
		String city_nm_o;
		String postal_cd_o;            
		String state_nm_o;
		String country_code_o;
		String country_nm_o;
		String telephone_no_1_o;
		String telephone_no_2_o;
		String telephone_no_3_o;
		String fax_no_1_o;
		String fax_no_2_o;
		String email_address_1_o;
		String email_address_2_o;
		String email_address_3_o;
		String delivery_mode_opt_ind_o;
		String delivery_mode_opt_date_o;
		String post_card_opt_ind_o;
		
		long mailAddress_id;
		long telephone_id_1;
		long telephone_id_2;
		long telephone_id_3;	
		long fax_id_1;
		long fax_id_2;
		long email_id_1;
		long email_id_2;
		long email_id_3;

		String name_line_one_n; 
		String name_line_two_n;       
		String street_line_one_n;      
		String street_line_two_n;      
		String city_nm_n;              
		String postal_cd_n;           
		String state_nm_n;       
		String country_code_n;
		String country_nm_n;
		String telephone_no_1_n;
		String telephone_no_2_n;
		String telephone_no_3_n;
		String fax_no_1_n;
		String fax_no_2_n;
		String email_address_1_n;
		String email_address_2_n;
		String email_address_3_n;
		String delivery_mode_opt_ind_n;
		String delivery_mode_opt_date_n;
		String post_card_opt_ind_n;
		
		String reg_no_to_delete;      
		String reg_no_to_insert;
		String customer_no;            
		String distinguished_nm;       
		String change_req_regn_no;     
		String change_req_user_nm;    
		String change_req_submit_dt;
		String date_printed;           
		String date_responded;         
		String last_modified_ts;       
		String user_id_printed;       
		String user_id_responded;      
		String change_address_in;
		String point_of_contact_nm;
		String point_of_contact_phone;
		String point_of_contact_email;
		String system_id;
		String changeRequestStatus_text;


}
