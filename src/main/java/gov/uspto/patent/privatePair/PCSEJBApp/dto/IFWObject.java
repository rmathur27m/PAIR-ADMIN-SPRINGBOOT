package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import java.util.List;

public class IFWObject {
    public String status;
    public List<Object> errorBag;
    public List<ResultBag> resultBag;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getErrorBag() {
        return errorBag;
    }

    public void setErrorBag(List<Object> errorBag) {
        this.errorBag = errorBag;
    }

    public List<ResultBag> getResultBag() {
        return resultBag;
    }

    public void setResultBag(List<ResultBag> resultBag) {
        this.resultBag = resultBag;
    }
}
