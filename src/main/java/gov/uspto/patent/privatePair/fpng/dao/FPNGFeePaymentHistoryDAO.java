package gov.uspto.patent.privatePair.fpng.dao;

import gov.uspto.patent.privatePair.exceptionhandlers.ApplicationException;
import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import gov.uspto.patent.privatePair.fpng.dto.Model;
import gov.uspto.patent.privatePair.fpng.dto.PaymentDetail;
import gov.uspto.patent.privatePair.fpng.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class FPNGFeePaymentHistoryDAO {

    private static final String DATE_FORMAT = "MM-dd-yyyy";

    @Autowired
    FpngRestClient fpngRestClient;

    @Value("${fpng.service.getFeeHistorybyReferenceNumberURL}")
     String urlString;

   @Value("${spring.datasource.oems.username}")
    String oemsUsername;

    @Value("${spring.datasource.oems.password}")
    String oemsPassword;

    public FpngServiceResponse getFeePaymentHistoryFromFpng(String appOrPatentNumber) throws ApplicationException {

        List<FeePaymentHistoryVo> feePaymentHistoryList = new ArrayList<FeePaymentHistoryVo>();
        long startTime = System.currentTimeMillis();
        FpngServiceResponse fpngServiceResponse = new FpngServiceResponse();
        try {
            /*
             * if ((log != null) && log.isDebugEnabled()) { log.
             * info("Retrieve the payment history from FPNG services using application or patent number :"
             * + appOrPatentNumber); }
             */
//		  String urlString = environment.getProperty("fpng.service.getFeeHistorybyReferenceNumberURL");
            fpngServiceResponse = fpngRestClient.getFeePaymentRecsByAppNum(appOrPatentNumber, urlString);
            if (fpngServiceResponse != null && fpngServiceResponse.getSuccess()) {
                feePaymentHistoryList = translateFPNGServiceResponseToVo(fpngServiceResponse);
            }
        } catch (Exception e) {
            //if (log == null) {
            throw new ApplicationException("Exception while retrieving the fee payment history :", e);
            //}

            //log.error("Error occured while retrieving the fee payment history from FPNG" + e.getMessage());
            //if (activityLogger != null) {
            //	addIfwActivity(appOrPatentNumber, Statistics.VIEW_FPNG_RESULTS_REST, startTime, null,
            //			Statistics.ERROR_RESULT, 0, 0, "0", activityLogger);
            //}

            //throw new ApplicationException("Exception while retrieving the fee payment history :", e);
        }
        return fpngServiceResponse;
    }

    private List<FeePaymentHistoryVo> translateFPNGServiceResponseToVo(FpngServiceResponse fpngServiceResponse) {
        List<FeePaymentHistoryVo> feePaymentHistoryTranslatelist = new ArrayList<FeePaymentHistoryVo>();

        for (Model model : fpngServiceResponse.getModel()) {
            if (model.getResults() != null) {
                for (Result result : model.getResults()) {
                    FeePaymentHistoryVo feePaymentHistoryVo = new FeePaymentHistoryVo();
                    feePaymentHistoryVo.setAccountingDate(convertMilliSecondsToString(result.getDatePosted()));
                    feePaymentHistoryVo.setFeeCode(result.getFeeCode());
                    feePaymentHistoryVo.setFeeAmount(result.getFeeAmount());
                    feePaymentHistoryVo.setFeequantity(result.getQuantity());
                    feePaymentHistoryVo.setTotalAmount(result.getAmount());
                    feePaymentHistoryVo.setMailRoomDate(convertMilliSecondsToString(result.getMailRoomDate()));
                    translatePaymentDetailsToVo(result, feePaymentHistoryVo);
                    feePaymentHistoryTranslatelist.add(feePaymentHistoryVo);
                }
            }
        }

        return feePaymentHistoryTranslatelist;
    }

    private String convertMilliSecondsToString(Long datePosted) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(datePosted);

        return sdf.format(c.getTime());
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private FeePaymentHistoryVo translatePaymentDetailsToVo(Result result, FeePaymentHistoryVo feePaymentHistoryVo) {
        if (result.getPaymentDetails() != null) {
            SortedSet paymentMethodTypesSet = new TreeSet();
            for (PaymentDetail paymentDetail : result.getPaymentDetails()) {
                paymentMethodTypesSet.add(paymentDetail.getPaymentMethodType());
            }
            String commaSeparatedPaymentTypes = StringUtils.collectionToCommaDelimitedString(paymentMethodTypesSet);
            feePaymentHistoryVo.setPaymentMethod(commaSeparatedPaymentTypes);
        }
        return feePaymentHistoryVo;
    }

    /**
     * @param args
     * @throws ApplicationException This function apply only for Patent with more than 7 digits
     */
    public FpngServiceResponse getFeePaymentHistoryFromFpng(String appOrPatentNumber,
                                                            String issueDate, int patentFlag) throws ApplicationException {
        List<FeePaymentHistoryVo> feePaymentHistoryList = new ArrayList<FeePaymentHistoryVo>();
        long startTime = System.currentTimeMillis();
        FpngServiceResponse fpngServiceResponse = new FpngServiceResponse();
        try {

            fpngServiceResponse = fpngRestClient.getFeePaymentRecsByAppNum(appOrPatentNumber, issueDate,
                    patentFlag, urlString);
            if (fpngServiceResponse != null && fpngServiceResponse.getSuccess()) {
                feePaymentHistoryList = translateFPNGServiceResponseToVo(fpngServiceResponse);
            }
        } catch (Exception e) {
        	throw new ApplicationException("Exception while retrieving the fee payment history :", e);
        }
        return fpngServiceResponse;
    }
}
