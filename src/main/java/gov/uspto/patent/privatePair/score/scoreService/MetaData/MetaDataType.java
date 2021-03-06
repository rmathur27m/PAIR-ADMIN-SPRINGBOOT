//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.02 at 12:25:58 AM EDT 
//


package gov.uspto.patent.privatePair.score.scoreService.MetaData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for meta-data-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="meta-data-type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="mega-item" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="mega-item-total" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="version-number" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="subversion-number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mailroomDateText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "meta-data-type", propOrder = {
    "megaItem",
    "megaItemTotal",
    "versionNumber",
    "subversionNumber",
    "mailroomDateText"
})
public class MetaDataType {

    @XmlElement(name = "mega-item", required = true)
    protected String megaItem;
    @XmlElement(name = "mega-item-total")
    protected long megaItemTotal;
    @XmlElement(name = "version-number", required = true)
    protected String versionNumber;
    @XmlElement(name = "subversion-number")
    protected String subversionNumber;
    protected String mailroomDateText;

    /**
     * Gets the value of the megaItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMegaItem() {
        return megaItem;
    }

    /**
     * Sets the value of the megaItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMegaItem(String value) {
        this.megaItem = value;
    }

    /**
     * Gets the value of the megaItemTotal property.
     * 
     */
    public long getMegaItemTotal() {
        return megaItemTotal;
    }

    /**
     * Sets the value of the megaItemTotal property.
     * 
     */
    public void setMegaItemTotal(long value) {
        this.megaItemTotal = value;
    }

    /**
     * Gets the value of the versionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * Sets the value of the versionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionNumber(String value) {
        this.versionNumber = value;
    }

    /**
     * Gets the value of the subversionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubversionNumber() {
        return subversionNumber;
    }

    /**
     * Sets the value of the subversionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubversionNumber(String value) {
        this.subversionNumber = value;
    }

    /**
     * Gets the value of the mailroomDateText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailroomDateText() {
        return mailroomDateText;
    }

    /**
     * Sets the value of the mailroomDateText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailroomDateText(String value) {
        this.mailroomDateText = value;
    }

}
