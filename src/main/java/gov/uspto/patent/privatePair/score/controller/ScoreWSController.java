package gov.uspto.patent.privatePair.score.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import gov.uspto.patent.privatePair.score.client.*;
import gov.uspto.patent.privatePair.score.scoreService.AppListImpl.ApplicationNumbersType;
import gov.uspto.patent.privatePair.score.scoreService.AppListImpl.DateRange;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemDetailImpl.MegaItemContentType;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemDetailImpl.MegaItemDetailInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemDetailImpl.MegaItemDetailsType;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemDetailImpl.SequenceListNumbersType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemRecordType;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemRecordsType;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemsInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVerHistoryInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVersionType;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVersionsType;
import gov.uspto.patent.privatePair.score.scoreService.MetaData.MetaDataRecordsType;
import gov.uspto.patent.privatePair.score.scoreService.MetaData.MetaDataType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@RestController
public class ScoreWSController {

	@Autowired
	private ScoreSoapClient scoreSoapClient;

	@Autowired
	private ScoreMegaItemImpl scoreMegaItemImpl;

	@Autowired
	private ScoreMegaItemVersionHistoryImpl scoreMegaItemVersionHistoryImpl;

	@Autowired
	private ScoreAppListImpl scoreAppListImpl;

	@Autowired
	private ScoreMegaDetailImpl scoreMegaDetailImpl;

	@PostMapping(value ="/getMetaData", produces = {"application/json"})
	public List<MetaDataType> getMetaData(@RequestBody Map ApplicationNumbersType) {
		//ApplicationNumbersType applicationNum = new ApplicationNumbersType();
		//applicationNum.getApplicationNumber().add(applicationNumber);
		MetaDataRecordsType mRecordsType= new MetaDataRecordsType();
		mRecordsType= scoreSoapClient.getMetaData(ApplicationNumbersType.get("ApplicationNumbersType").toString());
		List<MetaDataType> list = new ArrayList<MetaDataType>();
		list= mRecordsType.getMetaDataRecord();
		return list;
	}


	@PostMapping(value ="/getMegaItems", produces = {"application/json"})
	public List<MegaItemRecordType> getMegaItems(@RequestBody Map mIIM_Map) {
		MegaItemsInputMessage mIIM = new MegaItemsInputMessage();
		mIIM.setApplictionNumber(mIIM_Map.get("ApplictionNumber").toString());
		mIIM.setMegaItemType(mIIM_Map.get("MegaItemType").toString());
		mIIM.setVersionNumber(mIIM_Map.get("VersionNumber").toString());
		mIIM.setSubVersionNumber(mIIM_Map.get("SubVersionNumbe").toString());
		MegaItemRecordsType mRecordsType= new MegaItemRecordsType();
		mRecordsType= scoreMegaItemImpl.getMegaItems(mIIM);
		List<MegaItemRecordType> list = new ArrayList<MegaItemRecordType>();
		list= mRecordsType.getMegaItemRecord();
		return list;
	}

	@PostMapping(value ="/getMegaItemVersionHistory", produces = {"application/json"})
	public List<MegaItemVersionType> getMegaItemVersionHistory(@RequestBody Map mIVHIM) {
		MegaItemVerHistoryInputMessage mIIM = new MegaItemVerHistoryInputMessage();
		mIIM.setApplicationNumber(mIVHIM.get("ApplictionNumber").toString());
		mIIM.setMegaItemType(mIVHIM.get("MegaItemType").toString());

		MegaItemVersionsType mRecordsType= new MegaItemVersionsType();
		mRecordsType= scoreMegaItemVersionHistoryImpl.getMegaItemVersionHistory(mIIM);
		List<MegaItemVersionType> list = new ArrayList<MegaItemVersionType>();
		list= mRecordsType.getMegaItemVersion();
		return list;
	}

	@PostMapping(value ="/getApplicationList", produces = {"application/json"})
	public List<String> getApplicationList(@RequestBody Map beginEndDates) throws ParseException {
		DateRange mIIM = new DateRange();
		mIIM.setBeginDate(convertStringToXMLGregorian(beginEndDates.get("beginDate").toString()));
		mIIM.setEndDate(convertStringToXMLGregorian(beginEndDates.get("endDate").toString()));
		ApplicationNumbersType mRecordsType= new ApplicationNumbersType();
		mRecordsType= scoreAppListImpl.getApplicationList(mIIM);
		List<String> list = new ArrayList<String>();
		list= mRecordsType.getApplicationNumber();
		return list;
	}

	@PostMapping(value ="/getMegaItemDetail", produces = {"application/json"})
	public List<MegaItemContentType> getMegaItemDetail(@RequestBody Map megaItemDetailInputMessage) throws ParseException {
		MegaItemDetailInputMessage mIIM = new MegaItemDetailInputMessage();
		mIIM.setMegaItemId(megaItemDetailInputMessage.get("MegaItemId").toString());
		List<LinkedHashMap> sequenceNumber = new ArrayList<LinkedHashMap>();
		sequenceNumber=(List<LinkedHashMap>)megaItemDetailInputMessage.get("SequenceNumbers");
		SequenceListNumbersType  sequenceListNumbersType = new SequenceListNumbersType();
		for (LinkedHashMap str :sequenceNumber) {
			sequenceListNumbersType.getSequenceNumber().add(str.get("sequence-number").toString());
		}
		mIIM.setSequenceNumbers(sequenceListNumbersType);
		MegaItemDetailsType mRecordsType= new MegaItemDetailsType();
		mRecordsType= scoreMegaDetailImpl.getMegaItemDetail(mIIM);
		List<MegaItemContentType> list = new ArrayList<MegaItemContentType>();
		list= mRecordsType.getMegaItemContent();
		return list;
	}

	public XMLGregorianCalendar convertStringToXMLGregorian(String dateStr) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(dateStr);

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		XMLGregorianCalendar xmlGregCal = null;
		try {
			xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return xmlGregCal;
	}


}