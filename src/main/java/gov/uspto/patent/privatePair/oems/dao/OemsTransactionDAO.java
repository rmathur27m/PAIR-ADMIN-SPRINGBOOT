package gov.uspto.patent.privatePair.oems.dao;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OemsTransactionDAO {

    @Value("${spring.datasource.oems.url}")
    String oemsDataSourceUrl;

    @Value("${spring.datasource.oems.username}")
    String oemsUsername;

    @Value("${spring.datasource.oems.password}")
    String oemsPassword;

    @Value("${spring.datasource.secondary.jndi-name}")
    String oemsJNDI;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private JndiDataSourceLookup lookup = new JndiDataSourceLookup();

    SimpleJdbcInsert simpleJdbcInsert;
    
    /*
     * The  method "insertOemsTransactionData" inserts OEMS transaction details data into the PRIVATE_PAIR_ORDERS
     * table in OEMS DB
     */
    
    @Autowired
    public DataSource dataSource() throws SQLException {
        //OracleDataSource dataSource = new OracleDataSource();
       final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl(oemsDataSourceUrl);
        dataSource.setUsername(oemsUsername);
        dataSource.setPassword(oemsPassword);
        this.setDataSource(dataSource);

        return dataSource;//lookup.getDataSource(oemsJNDI);
    }
    
   
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("SECURE_ORDER_VALIDATION");
       
    }
    
    public int  insertOemsTransactionData(String transactionId,
			 String documentID, 
			 int customerNumber) throws Exception {
       String userId = "PPAIR";
       int result=0;
       final Map<String, Object> parameters = new HashMap<String, Object>();
       try {    	  
    	   
    	   result=jdbcTemplate.update(
    			      "INSERT INTO SECURE_ORDER_VALIDATION VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", 
    			      transactionId, documentID, "P", customerNumber, new
    					  java.sql.Timestamp(System.currentTimeMillis()), new
    					  java.sql.Timestamp(System.currentTimeMillis()), userId, null, null);			
       }catch(Exception ex){
           System.out.println("Exception caught in OEMS DAO-->"+ex.getMessage());
       }       
       return result;
   }
    
    
    
    


}
