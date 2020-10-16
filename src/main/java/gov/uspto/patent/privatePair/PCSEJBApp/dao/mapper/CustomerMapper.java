package gov.uspto.patent.privatePair.PCSEJBApp.dao.mapper;


import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustomerVo;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Mapper
public interface CustomerMapper {
    public int customerValidation(String customerNo)throws Exception;


    @Select("select count(*) FROM PILOT_CUSTOMER WHERE CUSTOMER_NO = #{customerNo}")
    public int checkCustomer(String customerNo)throws Exception;


    public int saveCustomer(CustomerVo customerVO) throws ServiceException;


	/*
	 * @Select("SELECT PILOT_CUSTOMER_ID,CUSTOMER_NO,REQUESTOR_FULL_NM,REQUESTOR_REGISTRATION_NO,REQUESTOR_TELEPHONE_NO,"
	 * +" REQUESTOR_EXT_NO,REQUESTOR_EMAIL_TX,CONTACT_FULL_NM,CONTACT_REGISTRATION_NO,CONTACT_TELEPHONE_NO,CONTACT_EXT_NO,"
	 * +" CONTACT_EMAIL_TX,ACTIVE_IN,ADD_TS,REMOVE_TS,CREATE_TS,CREATE_USER_ID,LAST_MOD_TS,LAST_MOD_USER_ID"
	 * + " FROM PILOT_CUSTOMER" + " WHERE CUSTOMER_NO = #{customerNo}")
	 */    
    public CustomerVo getCustomerInfo(String customerNo) throws ServiceException;

    public int insertCustomerHistory(CustomerVo customerVO) throws ServiceException;


    public int updateCustomer(CustomerVo customerVO) throws ServiceException;
    
    @Select("select MIN(CREATE_TS) FROM PILOT_CUSTOMER")
    public java.sql.Date earliestDateSQL();
    
    /*public List<CustomerVo> getCustomersList(@Param("customerNumber") String customerNumber,
    		@Param("startDate") Date startDate, 
    		@Param("endDate") String endDate,
    		@Param("sortOrder") String sortOrder) throws ServiceException;*/
	public List<CustomerVo> getCustomersList(@Param("customerNumber") String customerNumber,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") String endDate,
                                             @Param("sortOrder") String sortOrder) throws ServiceException;
    
	/*
	 * @Select("SELECT FK_PILOT_CUSTOMER_ID,CUSTOMER_NO,REQUESTOR_FULL_NM,REQUESTOR_REGISTRATION_NO,REQUESTOR_TELEPHONE_NO, REQUESTOR_EXT_NO, REQUESTOR_EMAIL_TX,CONTACT_FULL_NM,CONTACT_REGISTRATION_NO, CONTACT_TELEPHONE_NO,CONTACT_EXT_NO, CONTACT_EMAIL_TX,ACTIVE_IN,ADD_TS,REMOVE_TS,CREATE_TS,CREATE_USER_ID,LAST_MOD_TS,LAST_MOD_USER_ID FROM PILOT_CUSTOMER_HISTORY WHERE CUSTOMER_NO = #{customerNo} ORDER BY CREATE_TS DESC"
	 * )
	 */
    public List<CustomerVo> getCustomerHistoryList(String customerNo) throws ServiceException, SQLException;
    
}
