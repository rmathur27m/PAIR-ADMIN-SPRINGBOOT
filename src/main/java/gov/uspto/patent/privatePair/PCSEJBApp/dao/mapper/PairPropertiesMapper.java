package gov.uspto.patent.privatePair.PCSEJBApp.dao.mapper;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.PairPropertyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface PairPropertiesMapper {

    @Select("SELECT distinct TRIM(PROPERTY_GROUP_NM) from PAIR_PROPERTY where PROPERTY_MODE_TX != #{reverseMode} AND PROPERTY_NM = #{propertyName}")
    public  String pairPropertiesGroupQry(String reverseMode, String propertyName);


    public List<PairPropertyVO> getPairPropertiesData(@Param("reverseMode") String reverseMode,
                                                      @Param("groupName") String groupName,
                                                      @Param("propertyName") String propertyName,
                                                      @Param("propertyNameStr") String propertyNameStr) throws Exception;
}
