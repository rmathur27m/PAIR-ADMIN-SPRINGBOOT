//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.02 at 02:02:36 PM EDT 
//


package gov.uspto.patent.privatePair.score.scoreService.MegaData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for megaItemsInput-message complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="megaItemsInput-message"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ApplictionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="VersionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MegaItemType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="SubVersionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "megaItemsInput-message", propOrder = {
    "applictionNumber",
    "versionNumber",
    "megaItemType",
    "subVersionNumber"
})
public class MegaItemsInputMessage {

    @XmlElement(name = "ApplictionNumber", required = true)
    protected String applictionNumber;
    @XmlElement(name = "VersionNumber", required = true)
    protected String versionNumber;
    @XmlElement(name = "MegaItemType", required = true)
    protected String megaItemType;
    @XmlElement(name = "SubVersionNumber", required = true)
    protected String subVersionNumber;

    /**
     * Gets the value of the applictionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplictionNumber() {
        return applictionNumber;
    }

    /**
     * Sets the value of the applictionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplictionNumber(String value) {
        this.applictionNumber = value;
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
     * Gets the value of the megaItemType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMegaItemType() {
        return megaItemType;
    }

    /**
     * Sets the value of the megaItemType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMegaItemType(String value) {
        this.megaItemType = value;
    }

    /**
     * Gets the value of the subVersionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubVersionNumber() {
        return subVersionNumber;
    }

    /**
     * Sets the value of the subVersionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubVersionNumber(String value) {
        this.subVersionNumber = value;
    }

}
