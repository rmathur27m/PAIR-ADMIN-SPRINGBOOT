package gov.uspto.patent.privatePair.exceptionhandlers;
/**
 * Thrown when an application error occurs, which should be mentioned to the user in a friendly way
 * by showing ApplicationError.jsp
 * @author: Ben Verloop
 * @modelguid {B1DF53B9-13D3-49A5-91F5-9040B9E622BB}
 */
public class ApplicationException extends Exception {
	
	/** @modelguid {55F7238F-3751-484D-8FBA-6C612E59EC9B} */
	int reason= -1;
	
    /** DocException constructor. 
     * @modelguid {352D4EAF-CC2D-4DD6-AFE3-8BCEEC623319}
     */
    public ApplicationException() {
        super();
    }

    /**
     * @param s java.lang.String
     * @modelguid {D0FA63CF-BD59-4934-AF64-FBF489BAB2BA}
     */
    public ApplicationException(String s, int r) {
        super(s);
        reason=r;
    }
    
    public ApplicationException(String string, Exception e) {
        super(string, e);
    }

    public ApplicationException(Exception e) {
        super(e);
    }
    
    /** @modelguid {2BDAE172-2A17-42FD-9ECE-23E1DB4D8C7A} */
    public int getReason() {
    	return reason;
    }
}
