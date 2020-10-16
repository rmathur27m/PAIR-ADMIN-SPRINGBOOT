package gov.uspto.utils;

//import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;

/**
 * Static helper class used in logging.
 * 
 */
public class Statistics {

    public static final String STATISTICS_SEPARATOR = ","; // is referenced in
    public static final String STATISTICS_YES = "Yes";
    public static final String CLIENTKEYUSERNAME = "ClientKeyUserName";
    private static boolean isConfigured = false; // is referenced in static
                                                 // method, so must be static
    @SuppressWarnings("unused")
    private static String anonymousUserid = null;

    public static final int SAVENEWCUSTOMERNUMBERREQUEST = 69;
    public static final int UPDATENEWCUSTOMERNUMBERREQUEST = 70;
    public static final int DELETENEWCUSTOMERNUMBERREQUEST = 71;

    public static final int SAVEENTITYCHANGEREQUEST = 72;
    public static final int UPDATEENTITYCHANGEREQUEST = 73;
    public static final int DELETEENTITYCHANGEREQUEST = 74;
    public static final int TRANSMITENTITYCHANGEREQUEST = 75;

    public static final int TRANSMITUPDATEAPPLICATIONADDRESSREQUEST = 76;
    public static final int UPDATEANUPDATEAPPLICATIONADDRESSREQUEST = 77;
    public static final int PREVIEWUPDATEAPPLICATIONADDRESSREQUEST = 78;
    public static final int DELETEUPDATEAPPLICATIONADDRESSREQUEST = 79;
    public static final int SAVEUPDATEAPPLICATIONADDRESSREQUEST = 80;

    public static final int VIEWSAVECOMPLETEREQUESTLIST = 81;
    public static final int VIEWENTITYSTATUSREQUEST = 82;

    public static final int PRIVIEWNEWCUSTOMERNUMBERREQUEST = 83;
    public static final int TRANSMITNEWCUSTOMERNUMBERREQUEST = 84;
    public static final int VIEWNEWCUSTOMERNUMBERREQUEST = 85;
    public static final int IMPORTNEWCUSTOMERNUMBERREQUEST = 86;

    public static final int PRIVIEWENTITYSTATUSREQUEST = 87;
    
    public static final int VIEWUPDATEADDRESSREQUEST = 88;
    
    public static final int APPLICATIONDATAREQUEST = 1;
    public static final int TRANSACTIONHISTORYREQUEST = 2;
    public static final int CONTINUITYDATAREQUEST = 3;
    public static final int CORRESPONDENCEADDRESSDATAREQUEST = 4;
    public static final int MAINTENANCEFEEDATAREQUEST = 13;
    public static final int ATTORNEYADDRESSDATAREQUEST = 5;
    public static final int FEESDATAREQUEST = 6;
    public static final int FOREIGNPRIORITYDATAREQUEST = 7;
    public static final int PATENTTERMDATAREQUEST = 8;
    public static final int ASSIGNMENTSDATAREQUEST = 9;
    public static final int IMAGEFILEWRAPPERDATAREQUEST = 10;
    public static final int SUPPLEMENTDATAREQUEST = 11;
    public static final int FIRSTACTIONPREDICTIONDATAREQUEST = 4;
    public static final int PUBLICATIONREVIEWDATAREQUEST = 4;
    public static final int WIPOAPPLICATIONDATAREQUEST = 12;
    //private static Logger statsAdminLogger = null;
    public static int updateApplicationAddress;

    public SortMe sortMe = new SortMe();

    public static final String[] statisticTypes = {
            "GetDossier", // 0
            "GetPdfPage",
            "DownloadPdfBook",
            "UserRequestedScanning",
            "UserNotRequestedScanning",
            "ApplicationData", //1
            "TransactionHistory", //2
            "ImageFileWrapper",//10
            "PatentTermAdjustments",//8
            "PatentTermExtensionHistory", //8
            "ContinuityData", // 3
            "ForeignPriorityData", //7
            "Fees", //6
            "Publish Documents",//1
            "ApplicationNumberSearch",
            "PatentNumberSearch",
            "PublicationNumberSearch",
            "",
            "AttyAgentAddress", //5
            "SupplementalContent",
            "Sequence Version History", // 20
            "Sequence Version History Download",
            "ViewingAHD",
            "ePatentReferences",
            "PublicationReview",//12
            "FirstActionPrediction",//11
            "ApplicationsByCustomerNumber", // 26
            "CustomerNumberDetails",
            "CustomerOutGoingCorrespondence", // 28
            "Attorney Docket Details",
            "Select New Case", // 30
            "Customer Change Request Form By CN", "Customer Preview Changes Form By CN",
            "Customer Review Corrections Submit Form By CN",
            "CustomerNumberDetails",
            "CustomerOutGoingCorrespondence",
            "Customer Details Print Form By CN",
            "Examiner Link",
            "First Action Prediction",
            "First Action Prediction Print Letter",
            "ePRLaunch", // 40
            "ControlNumberSearch", "IFWReviewOnCustomerSearch", "AttorneyDocketSearch", "PrinterFriendly", "PCT Number Search",
            "View Sequences",
            "View Megatables",
            "View SearchResults",
            "View CPLS",
            "View Other Supplemental Items", // 50
            "Download PDF Document", "View PDF Document", "Single Application Data Download",
            "Multiple Application Data Download", "Attorney Docket Update", "Attorney Docket Exact Search",
            "Attorney Docket WildCard Search", "Reached USPTO Download Policy",
            "Outgoing Correspondence XML Download",
            "Outgoing Correspondence PDF Download", // 60
            "XML Download for Staus Changes By Customer Number", "XML Download for OCN By Customer Number",
            "XML Download for Customer Number", "EBC Announcements Tab", "371RelatedAppTab", "Fee Maint Link", // 66
            "DMNumberSearch", "vieweNotifications", // 68
            "saveNewCustomerRequest", // 69
            "updateNewCustomerRequest", // 70
            "deleteNewCustomerRequest", // 71 Only add more but never delete any
                                        // element in this array total 71 on
                                        // 2015-01-29
            "saveEntityRequestChange", // 72
            "updateEntityRequestChange", // 73
            "deleteEntityRequestChange", // 74
            "transmitEntityRequestChange", // 75
            "transmitUpdateApplicationAddressRequest", // 76
            "updateAnUpdateApplicationAddressRequest", // 77
            "previewUpdateApplicationAddressRequest", // 78
            "deleteUpdateApplicationAddressRequest", // 79
            "saveUpdateApplicationAddressRequest", // 80
            "viewSaveCompleteRequestList", // 81
            "viewEntityStatusRequest", // 82
            "previewNewCustomerRequest", // 83
            "transmitNewCustomerRequest", // 84
            "viewNewCustomerRequest", // 85
            "importNewCustomerRequest", // 86
            "priviewEntityRequestChange", // 87
            "viewUpdateAddressRequest" // 88
    };

    public Statistics() {
    }

    private static synchronized void init() {

        isConfigured = true;
        //statsAdminLogger = LoggingLevels.getMyLogger(Statistics.class.getName() + "1");
        anonymousUserid = CLIENTKEYUSERNAME;
    }

    /**
     * Log activity.
     * 
     * @param number
     *            Request Id
     * @param DN
     *            Distinguished Name
     * @param typeOfRequest
     *            Request Type
     * @param activityStartTime
     *            Start of activity
     * @param activityEndTime
     *            End of activity
     * @param bytesDownloaded
     * @param startTimeMillis
     *            Activity duration in milliseconds
     * @param result
     *            not used
     * @param runMode
     *            not used
     * @param remoteIPAddress
     *            IP address of client
     */
    public static String addPairAdminActivity(String number, String DN, int typeOfRequest, String activityStartTime,
            String activityEndTime, String bytesDownloaded, long startTimeMillis,

            int result, String runMode, String remoteIPAddress) {

        if (!isConfigured)
            init();

        // date and time
        StringBuilder sb = new StringBuilder();
        sb.append(SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat());
        sb.append(STATISTICS_SEPARATOR);

        // request number
        sb.append(number);
        sb.append(STATISTICS_SEPARATOR);

        sb.append(DN);
        sb.append(STATISTICS_SEPARATOR);

        // activity
        sb.append(statisticTypes[typeOfRequest]);
        sb.append(STATISTICS_SEPARATOR);

        // activity start time
        sb.append(activityStartTime);
        sb.append(STATISTICS_SEPARATOR);

        // activity end time
        sb.append(activityEndTime);
        sb.append(STATISTICS_SEPARATOR);

        // activity byte download may not be using in this time but need the
        // same standard format with Private PAIR
        sb.append(bytesDownloaded);
        sb.append(STATISTICS_SEPARATOR);

        // total time by milliseconds
        sb.append(System.currentTimeMillis() - startTimeMillis);

        sb.append(STATISTICS_SEPARATOR);
        sb.append(remoteIPAddress);

        //statsAdminLogger.info(sb.toString());
        return sb.toString();
    }

}