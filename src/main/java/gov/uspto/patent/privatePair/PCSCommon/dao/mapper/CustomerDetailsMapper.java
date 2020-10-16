package gov.uspto.patent.privatePair.PCSCommon.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import gov.uspto.patent.privatePair.utils.ServiceException;

@Mapper
public interface CustomerDetailsMapper {

	 @Select("select dn.pair_user_dn_id regnum from pair_user_dn dn where dn.dn= #{dn}")
	 List<String> getRegistrationNumber(String dn) throws ServiceException;
}
