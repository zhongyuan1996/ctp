import java.util.HashMap;
import java.util.Map.Entry;

import ctp.thosttraderapi.*;

public class TdspiImpl extends CThostFtdcTraderSpi{

    private String t_BrokerId;
    private String t_UserId;
    private String t_PassWord;
    public HashMap<String, Order> orderlist= new HashMap<String, Order>();
    public HashMap<String, Position> positionlist = new HashMap<String, Position>();
    public HashMap<String, String> instrumentAndExchange = new HashMap<String, String>();
    public HashMap<String, Integer> positionbuffer = new HashMap<String, Integer>();
    basestrat base = new stratexp(this);

    public int front_id;
    public int session_id;
    public String instruementids;
    public CThostFtdcTraderApi t_tdapi;
    public int request_id = 0;
    public String exchange_id;
    

    public TdspiImpl(CThostFtdcTraderApi tdapi) {
        this.t_tdapi = tdapi;
    }

    public void OnFrontConnected(){
        CThostFtdcReqUserLoginField field = new CThostFtdcReqUserLoginField();
        field.setBrokerID(t_BrokerId);
        field.setUserID(t_UserId);
        field.setPassword(t_PassWord);
        t_tdapi.ReqUserLogin(field, 1);
        
    }
    

    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
    	
    	if (pRspInfo != null && pRspInfo.getErrorID() != 0){
    		
			System.out.printf("OnRspUserLogin ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());

			return;
		}
    	
        if (pRspUserLogin != null) {
            System.out.printf("TD Brokerid[%s]\n",pRspUserLogin.getBrokerID()+", Login Successful: "+pRspUserLogin.getUserID()+","+nRequestID);
            System.out.printf("TD FrontID is[%d]SessionID is[%d]\n", pRspUserLogin.getFrontID(), pRspUserLogin.getSessionID());
            this.front_id = pRspUserLogin.getFrontID();
            this.session_id = pRspUserLogin.getSessionID();
        }
    }

    

    public void OnFrontDisconnected(int nReason) {

        super.OnFrontDisconnected(nReason);
        System.out.println("OnFrontDisconnected" + nReason);

    }

    public void OnHeartBeatWarning(int nTimeLapse){

        System.out.println("OnHeartBeatWarning");
        super.OnHeartBeatWarning(nTimeLapse);

    }

    public void reqQrySettlementInfo() {
    	
    	CThostFtdcQrySettlementInfoField req = new CThostFtdcQrySettlementInfoField();
    	req.setBrokerID(t_BrokerId);
    	req.setInvestorID(t_UserId);
    	t_tdapi.ReqQrySettlementInfo(req, ++request_id);
    	
      }
    
    public void reqSettlementInfoConfirm(){

        CThostFtdcSettlementInfoConfirmField req = new CThostFtdcSettlementInfoConfirmField();
        req.setBrokerID(t_BrokerId);
        req.setInvestorID(t_UserId);
        t_tdapi.ReqSettlementInfoConfirm(req, ++request_id);
        
    }

    public boolean reqAccount(){

        CThostFtdcQryTradingAccountField req = new CThostFtdcQryTradingAccountField();
        req.setBrokerID(t_BrokerId);
        req.setInvestorID(t_UserId);
        int rtn = t_tdapi.ReqQryTradingAccount(req, ++request_id);
        return rtn == 0;
    }

    public boolean reqPosition(){

        CThostFtdcQryInvestorPositionField req = new CThostFtdcQryInvestorPositionField();
        req.setBrokerID(t_BrokerId);
        req.setInvestorID(t_UserId);
        int rtn = t_tdapi.ReqQryInvestorPosition(req, ++request_id);
        return rtn == 0;
    }

    public boolean reqPositionDetail(){

        CThostFtdcQryInvestorPositionDetailField req = new CThostFtdcQryInvestorPositionDetailField();
        req.setBrokerID(t_BrokerId);
        req.setInvestorID(t_UserId);
        int rtn = t_tdapi.ReqQryInvestorPositionDetail(req, ++request_id);
        return rtn == 0;
    }
    
    public boolean reqQryOrder() {
    	
    	CThostFtdcQryOrderField req = new CThostFtdcQryOrderField();
    	req.setBrokerID(t_BrokerId);
        req.setInvestorID(t_UserId);
        int rtn = t_tdapi.ReqQryOrder(req, ++request_id);
        return rtn == 0;
    }
    
    public boolean reqQryInstrumentByExchangeID(String exchangeid){
    	
    	CThostFtdcQryInstrumentField req = new CThostFtdcQryInstrumentField();
    	
    	req.setInstrumentID("");
    	req.setExchangeID(exchangeid);
    	int rtn = t_tdapi.ReqQryInstrument(req, ++request_id);
        return rtn == 0;
    }
    
    public boolean reqQryOneInstrument(String instrumentID) {
    	CThostFtdcQryInstrumentField req = new CThostFtdcQryInstrumentField();
        //req.setInstrumentID(instrumentID);
        int rtn = t_tdapi.ReqQryInstrument(req, ++request_id);
        return rtn == 0;
    }
    
    public boolean reqQryInstrument() {
    	CThostFtdcQryInstrumentField req = new CThostFtdcQryInstrumentField();
        //req.setInstrumentID(instrumentID);
        int rtn = t_tdapi.ReqQryInstrument(req, ++request_id);
        return rtn == 0;
    }
    
    public boolean sendOrderBuyOpen(String InstrumentId, int volume, double price) {
    	
    	CThostFtdcInputOrderField ctp_input = new CThostFtdcInputOrderField();
    	ctp_input.setBrokerID(this.t_BrokerId);
        ctp_input.setInvestorID(this.t_UserId);
        ctp_input.setExchangeID(instrumentAndExchange.get(InstrumentId));
    	ctp_input.setInstrumentID(InstrumentId);
    	ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Buy);
    	ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_Open));
    	ctp_input.setCombHedgeFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_HF_Speculation));
        ctp_input.setVolumeTotalOriginal(volume);
        ctp_input.setContingentCondition(thosttraderapiConstants.THOST_FTDC_CC_Immediately);
        ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_AV);
        ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GFD);
        ctp_input.setMinVolume(1);
        ctp_input.setForceCloseReason(thosttraderapiConstants.THOST_FTDC_FCC_NotForceClose);
        ctp_input.setIsAutoSuspend(0);
        ctp_input.setUserForceClose(0);
        ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_LimitPrice);
        ctp_input.setLimitPrice(price);
    	
        int rtn = t_tdapi.ReqOrderInsert(ctp_input, 1);

        if (rtn == 1){
            System.out.println("Send Order failed");
            return false;
        }
        else{
            System.out.println("Buy Open order sent");
            return true;
        }
    }
    
    public boolean sendOrderSellOpen(String InstrumentId, int volume, double price) {
    	
    	CThostFtdcInputOrderField ctp_input = new CThostFtdcInputOrderField();
    	ctp_input.setBrokerID(this.t_BrokerId);
        ctp_input.setInvestorID(this.t_UserId);
    	ctp_input.setInstrumentID(InstrumentId);
    	ctp_input.setExchangeID(instrumentAndExchange.get(InstrumentId));
    	ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Sell);
    	ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_Open));
    	ctp_input.setCombHedgeFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_HF_Speculation));
        ctp_input.setVolumeTotalOriginal(volume);
        ctp_input.setContingentCondition(thosttraderapiConstants.THOST_FTDC_CC_Immediately);
        ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_AV);
        ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GFD);
        ctp_input.setMinVolume(1);
        ctp_input.setForceCloseReason(thosttraderapiConstants.THOST_FTDC_FCC_NotForceClose);
        ctp_input.setIsAutoSuspend(0);
        ctp_input.setUserForceClose(0);
        ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_LimitPrice);
        ctp_input.setLimitPrice(price);
    	
        int rtn = t_tdapi.ReqOrderInsert(ctp_input, 1);

        if (rtn == 1){
            System.out.println("Send Order failed");
            return false;
        }
        else{
            System.out.println("Sell Open order sent");
            return true;
        }
    }
    
    public boolean sendOrderBuyCloseTodayAutoSplit(String InstrumentId, int volume, double price, int which) throws InterruptedException {
    	
    	//automatic split order into two parts if volume is larger than avaliable volume when trying to offset
    	
    	String tempsell = InstrumentId + "_Sell";
    	//check if tempsell is in positionbuffer. if not, create one with volume 0
    	if (positionbuffer.containsKey(tempsell+"_Td") == false) {
    		positionbuffer.put(tempsell+"_Td", 0);
    	}
    	
    	int volumeLeft = positionlist.get(tempsell).todayVolumeAval - positionbuffer.get(tempsell+"_Td");
    	
    	if (volume > volumeLeft && which == 0) {
    		
    		boolean rtn1 = sendOrderBuyCloseToday(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempsell+"_Td", volumeLeft +positionbuffer.get(tempsell+"_Td"));
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderBuyCloseYdAutoSplit(InstrumentId, volume - volumeLeft, price, 1);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else if (volume > volumeLeft && which == 1) {
    		
    		boolean rtn1 = sendOrderBuyCloseToday(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempsell+"_Td", volumeLeft +positionbuffer.get(tempsell+"_Td")); 
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderBuyOpen(InstrumentId, volume - volumeLeft, price);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else{
    		
    		boolean rtn = sendOrderBuyCloseToday(InstrumentId, volume, price);
    		positionbuffer.put(tempsell+"_Td", volume +positionbuffer.get(tempsell+"_Td"));
    		
    		if (rtn == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    }
    
    public boolean sendOrderSellCloseTodayAutoSplit(String InstrumentId, int volume, double price, int which) throws InterruptedException {
    	
    	//automatic split order into two parts if volume is larger than avaliable volume when trying to offset
    	
    	String tempbuy = InstrumentId + "_Buy";
    	//check if tempsell is in positionbuffer. if not, create one with volume 0
    	if (positionbuffer.containsKey(tempbuy+"_Td") == false) {
    		positionbuffer.put(tempbuy+"_Td", 0);
    	}
    	
    	int volumeLeft = positionlist.get(tempbuy).todayVolumeAval - positionbuffer.get(tempbuy+"_Td");
    	
    	if (volume > volumeLeft && which == 0) {
    		
    		boolean rtn1 = sendOrderSellCloseToday(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempbuy+"_Td", volumeLeft +positionbuffer.get(tempbuy+"_Td"));
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderSellCloseYdAutoSplit(InstrumentId, volume - volumeLeft, price, 1);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else if (volume > volumeLeft && which == 1) {
    		
    		boolean rtn1 = sendOrderSellCloseToday(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempbuy+"_Td", volumeLeft +positionbuffer.get(tempbuy+"_Td")); 
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderSellOpen(InstrumentId, volume - volumeLeft, price);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else{
    		
    		boolean rtn = sendOrderSellCloseToday(InstrumentId, volume, price);
    		positionbuffer.put(tempbuy+"_Td", volume +positionbuffer.get(tempbuy+"_Td"));
    		
    		if (rtn == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    }
    
    public boolean sendOrderBuyCloseToday(String InstrumentId, int volume, double price) {
    	
    	//do not use this function. use auto split
    	
    	CThostFtdcInputOrderField ctp_input = new CThostFtdcInputOrderField();
    	ctp_input.setBrokerID(this.t_BrokerId);
        ctp_input.setInvestorID(this.t_UserId);
    	ctp_input.setInstrumentID(InstrumentId);
    	ctp_input.setExchangeID(instrumentAndExchange.get(InstrumentId));
    	ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Buy);
    	ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_CloseToday));
    	ctp_input.setCombHedgeFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_HF_Speculation));
        ctp_input.setVolumeTotalOriginal(volume);
        ctp_input.setContingentCondition(thosttraderapiConstants.THOST_FTDC_CC_Immediately);
        ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_AV);
        ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GFD);
        ctp_input.setMinVolume(1);
        ctp_input.setForceCloseReason(thosttraderapiConstants.THOST_FTDC_FCC_NotForceClose);
        ctp_input.setIsAutoSuspend(0);
        ctp_input.setUserForceClose(0);
        ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_LimitPrice);
        ctp_input.setLimitPrice(price);
    	
        int rtn = t_tdapi.ReqOrderInsert(ctp_input, 1);

        if (rtn == 1){
            System.out.println("Send Order failed");
            return false;
        }
        else{
            System.out.println("Buy Close Today order sent");
            return true;
        }
    }
    
    public boolean sendOrderSellCloseToday(String InstrumentId, int volume, double price) {
    	
    	//do not use this function, use auto split
    	
    	CThostFtdcInputOrderField ctp_input = new CThostFtdcInputOrderField();
    	ctp_input.setBrokerID(this.t_BrokerId);
        ctp_input.setInvestorID(this.t_UserId);
    	ctp_input.setInstrumentID(InstrumentId);
    	ctp_input.setExchangeID(instrumentAndExchange.get(InstrumentId));
    	ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Sell);
    	ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_CloseToday));
    	ctp_input.setCombHedgeFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_HF_Speculation));
        ctp_input.setVolumeTotalOriginal(volume);
        ctp_input.setContingentCondition(thosttraderapiConstants.THOST_FTDC_CC_Immediately);
        ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_AV);
        ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GFD);
        ctp_input.setMinVolume(1);
        ctp_input.setForceCloseReason(thosttraderapiConstants.THOST_FTDC_FCC_NotForceClose);
        ctp_input.setIsAutoSuspend(0);
        ctp_input.setUserForceClose(0);
        ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_LimitPrice);
        ctp_input.setLimitPrice(price);
    	
        int rtn = t_tdapi.ReqOrderInsert(ctp_input, 1);

        if (rtn == 1){
            System.out.println("Send Order failed");
            return false;
        }
        else{
            System.out.println("Sell Close Today order sent");
            return true;
        }
    }
    
    public boolean sendOrderBuyCloseYdAutoSplit(String InstrumentId, int volume, double price, int which) throws InterruptedException {
    	
    	//automatic split order into two parts if volume is larger than avaliable volume when trying to offset
    	
    	String tempsell = InstrumentId + "_Sell";
    	//check if tempsell is in positionbuffer. if not, create one with volume 0
    	if (positionbuffer.containsKey(tempsell+"_Yd") == false) {
    		positionbuffer.put(tempsell+"_Yd", 0);
    	}
    	
    	int volumeLeft = positionlist.get(tempsell).ydVolumeAval - positionbuffer.get(tempsell+"_Yd");
    	
    	if (volume > volumeLeft && which == 0) {
    		
    		boolean rtn1 = sendOrderBuyCloseYd(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempsell+"_Yd", volumeLeft +positionbuffer.get(tempsell+"_Yd"));
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderBuyCloseTodayAutoSplit(InstrumentId, volume - volumeLeft, price, 1);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else if (volume > volumeLeft && which == 1) {
    		
    		boolean rtn1 = sendOrderBuyCloseYd(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempsell+"_Yd", volumeLeft +positionbuffer.get(tempsell+"_Yd")); 
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderBuyOpen(InstrumentId, volume - volumeLeft, price);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else{
    		
    		boolean rtn = sendOrderBuyCloseYd(InstrumentId, volume, price);
    		positionbuffer.put(tempsell+"_Yd", volume +positionbuffer.get(tempsell+"_Yd"));
    		
    		if (rtn == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    }
    
    public boolean sendOrderSellCloseYdAutoSplit(String InstrumentId, int volume, double price, int which) throws InterruptedException {
    	
    	//automatic split order into two parts if volume is larger than avaliable volume when trying to offset
    	
    	String tempbuy = InstrumentId + "_Buy";
    	//check if tempsell is in positionbuffer. if not, create one with volume 0
    	if (positionbuffer.containsKey(tempbuy+"_Yd") == false) {
    		positionbuffer.put(tempbuy+"_Yd", 0);
    	}
    	
    	int volumeLeft = positionlist.get(tempbuy).ydVolumeAval - positionbuffer.get(tempbuy+"_Yd");
    	
    	if (volume > volumeLeft && which == 0) {
    		
    		boolean rtn1 = sendOrderSellCloseYd(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempbuy+"_Yd", volumeLeft +positionbuffer.get(tempbuy+"_Yd"));
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderSellCloseTodayAutoSplit(InstrumentId, volume - volumeLeft, price, 1);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else if (volume > volumeLeft && which == 1) {
    		
    		boolean rtn1 = sendOrderSellCloseYd(InstrumentId, volumeLeft, price);
    		positionbuffer.put(tempbuy+"_Yd", volumeLeft +positionbuffer.get(tempbuy+"_Yd")); 
    		
    		Thread.sleep(1000);
    		
    		boolean rtn2 = sendOrderSellOpen(InstrumentId, volume - volumeLeft, price);
    		
    		if (rtn1 == rtn2 & rtn1 == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    	else{
    		
    		boolean rtn = sendOrderSellCloseYd(InstrumentId, volume, price);
    		positionbuffer.put(tempbuy+"_Yd", volume +positionbuffer.get(tempbuy+"_Yd"));
    		
    		if (rtn == true) {
    			System.out.println("auto split order fail to send");
    			return false;
    		}
    		else {
    			System.out.println("auto split order send");
    			return true;
    		}
    	}
    	
    }
    
    public boolean sendOrderBuyCloseYd(String InstrumentId, int volume, double price) {
    	
    	String temp = InstrumentId + "_Sell";
    	
    	if (volume > positionlist.get(temp).ydVolume) {
    		
    		System.out.println("平昨手数大于持仓");
    		return false;
    		
    	}
    	
    	CThostFtdcInputOrderField ctp_input = new CThostFtdcInputOrderField();
    	ctp_input.setBrokerID(this.t_BrokerId);
        ctp_input.setInvestorID(this.t_UserId);
        ctp_input.setExchangeID(instrumentAndExchange.get(InstrumentId));
    	ctp_input.setInstrumentID(InstrumentId);
    	ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Buy);
    	ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_CloseYesterday));
    	ctp_input.setCombHedgeFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_HF_Speculation));
        ctp_input.setVolumeTotalOriginal(volume);
        ctp_input.setContingentCondition(thosttraderapiConstants.THOST_FTDC_CC_Immediately);
        ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_AV);
        ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GFD);
        ctp_input.setMinVolume(1);
        ctp_input.setForceCloseReason(thosttraderapiConstants.THOST_FTDC_FCC_NotForceClose);
        ctp_input.setIsAutoSuspend(0);
        ctp_input.setUserForceClose(0);
        ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_LimitPrice);
        ctp_input.setLimitPrice(price);
    	
        int rtn = t_tdapi.ReqOrderInsert(ctp_input, 1);

        if (rtn == 1){
            System.out.println("Send Order failed");
            return false;
        }
        else{
            System.out.println("Buy CloseYD order sent");
            return true;
        }
    }
    
    public boolean sendOrderSellCloseYd(String InstrumentId, int volume, double price) {
    	    	
    	CThostFtdcInputOrderField ctp_input = new CThostFtdcInputOrderField();
    	ctp_input.setBrokerID(this.t_BrokerId);
        ctp_input.setInvestorID(this.t_UserId);
        ctp_input.setExchangeID(instrumentAndExchange.get(InstrumentId));
    	ctp_input.setInstrumentID(InstrumentId);
    	ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Sell);
    	ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_CloseYesterday));
    	ctp_input.setCombHedgeFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_HF_Speculation));
        ctp_input.setVolumeTotalOriginal(volume);
        ctp_input.setContingentCondition(thosttraderapiConstants.THOST_FTDC_CC_Immediately);
        ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_AV);
        ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GFD);
        ctp_input.setMinVolume(1);
        ctp_input.setForceCloseReason(thosttraderapiConstants.THOST_FTDC_FCC_NotForceClose);
        ctp_input.setIsAutoSuspend(0);
        ctp_input.setUserForceClose(0);
        ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_LimitPrice);
        ctp_input.setLimitPrice(price);
    	
        int rtn = t_tdapi.ReqOrderInsert(ctp_input, 1);

        if (rtn == 1){
            System.out.println("Send Order failed");
            return false;
        }
        else{
            System.out.println("Sell CloseYD order sent");
            return true;
        }
    }
   
    public boolean sendOrder(OrderInput input){

        CThostFtdcInputOrderField ctp_input = new CThostFtdcInputOrderField();
        to_ctp(ctp_input, input);

        int rtn = t_tdapi.ReqOrderInsert(ctp_input, 1);

        if (rtn == 1){
            System.out.println("Send Order failed");
            return false;
        }
        else{
            System.out.println("Order Sent");
            return true;
        }
    }

    public final void to_ctp(CThostFtdcInputOrderField ctp_input, OrderInput input){

    	ctp_input.setBrokerID(this.t_BrokerId);
        ctp_input.setInvestorID(this.t_UserId);
        ctp_input.setInstrumentID(input.instrument_id);
        ctp_input.setExchangeID(instrumentAndExchange.get(input.instrument_id));

        to_ctp_direction(ctp_input,input);
        to_ctp_comb_offset(ctp_input,input);
        
        ctp_input.setCombHedgeFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_HF_Speculation));
        ctp_input.setVolumeTotalOriginal(input.volume);
        ctp_input.setContingentCondition(thosttraderapiConstants.THOST_FTDC_CC_Immediately);
        to_ctp_volume_condition(ctp_input,input);
        to_ctp_time_condition(ctp_input,input);
        ctp_input.setMinVolume(1);
        ctp_input.setForceCloseReason(thosttraderapiConstants.THOST_FTDC_FCC_NotForceClose);
        ctp_input.setIsAutoSuspend(0);
        ctp_input.setUserForceClose(0);
        to_ctp_price_type(ctp_input,input);
        ctp_input.setLimitPrice(input.limit_price);
    }

    public final void to_ctp_direction(CThostFtdcInputOrderField ctp_input, OrderInput input){

        if (input.side == "SideBuy"){
            ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Buy);
        }
        else if (input.side == "SideSell"){
            ctp_input.setDirection(thosttraderapiConstants.THOST_FTDC_D_Sell);
        }
    }

    public final void to_ctp_comb_offset(CThostFtdcInputOrderField ctp_input, OrderInput input){

        if (input.offset == "OffsetClose"){
            ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_Close));
        }
        else if (input.offset == "OffsetOpen"){
            ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_Open));
        }
        else if (input.offset == "OffsetCloseToday"){
            ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_CloseToday));
        }
        else if (input.offset == "OffsetCloseYesterday"){
            ctp_input.setCombOffsetFlag(Character.toString(thosttraderapiConstants.THOST_FTDC_OF_CloseYesterday));
        }
    }

    public final void to_ctp_volume_condition(CThostFtdcInputOrderField ctp_input, OrderInput input){

        if (input.volume_condition == "VolumeConditionAny") {
            ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_AV);
        }
        else if (input.volume_condition == "VolumeConditionMin"){
            ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_MV);
        }
        else if (input.volume_condition == "VolumeConditionAll"){
            ctp_input.setVolumeCondition(thosttraderapiConstants.THOST_FTDC_VC_CV);
        }
    }

    public final void to_ctp_time_condition(CThostFtdcInputOrderField ctp_input, OrderInput input){

        if (input.time_condition == "TimeConditionGFD"){
            ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GFD);
        }
        else if (input.time_condition == "TimeConditionIOC"){
            ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_IOC);
        }
        else if (input.time_condition == "TimeConditionGTC"){
            ctp_input.setTimeCondition(thosttraderapiConstants.THOST_FTDC_TC_GTC);
        }

    }

    public final void to_ctp_price_type(CThostFtdcInputOrderField ctp_input, OrderInput input){

        if (input.price_type == "PriceTypeLimit"){
            ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_LimitPrice);
        }
        else if (input.price_type == "PriceTypeAny"){
            ctp_input.setOrderPriceType(thosttraderapiConstants.THOST_FTDC_OPT_AnyPrice);
        }

    }
 
    public boolean cancelOrder(String orderRef){
    	
    	
    	CThostFtdcInputOrderActionField ctp_action = new CThostFtdcInputOrderActionField();
    	ctp_action.setBrokerID(orderlist.get(orderRef).brokerId);
    	ctp_action.setInvestorID(orderlist.get(orderRef).investorId);
    	ctp_action.setInstrumentID(orderlist.get(orderRef).instrumentId);
    	ctp_action.setOrderRef(orderlist.get(orderRef).orderRef);
    	ctp_action.setFrontID(orderlist.get(orderRef).frontId);
    	ctp_action.setSessionID(orderlist.get(orderRef).sessionId);
    	ctp_action.setOrderSysID(orderlist.get(orderRef).orderSysId);
    	ctp_action.setExchangeID(orderlist.get(orderRef).exchangeId);
    	ctp_action.setActionFlag(thosttraderapiConstants.THOST_FTDC_AF_Delete);
    	
 
        int rtn = t_tdapi.ReqOrderAction(ctp_action, 1);

        if (rtn == 1){
            System.out.println("Failed to send cancel signal");
            return false;
        }
        else{
        	
            return true;
        }

    }

    public void cancelAllOrder() {
    	
    	
    	for (Entry<String, Order> item : orderlist.entrySet()){
    		String key = item.getKey();
    		
    		cancelOrder(key);
    	}
    	
    		
    }
    
    public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        if (pRspInfo != null && pRspInfo.getErrorID() != 0)
		{
			System.out.printf("OnRspQryTradingAccount ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());

			return;
		}
		
		if (pTradingAccount != null){

            System.out.println("Trading Account Info Received:");
            System.out.printf("AccountID[%s]Balance[%f]FrozenMargin[%f]FrozenCash[%f]FrozenCommission[%f]\n",
    				pTradingAccount.getAccountID(),pTradingAccount.getBalance(),pTradingAccount.getFrozenMargin(),
    				pTradingAccount.getFrozenCash(),pTradingAccount.getFrozenCommission());
            
            Account accountdata = new Account();
            accountdata.accountId = pTradingAccount.getAccountID();
            accountdata.balance = pTradingAccount.getBalance();
            accountdata.frozenMargin = pTradingAccount.getFrozenMargin();
            accountdata.frozenCash = pTradingAccount.getFrozenCash();
            accountdata.frozenCommision = pTradingAccount.getFrozenCommission();
            
            base.onaccount(accountdata);
		}
		else
		{
			System.out.printf("Trading Account Info: Null");
		}
	}

    public void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){
        if (pRspInfo != null && pRspInfo.getErrorID() != 0){

            System.out.printf("OnRspQryInvestorPosition ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());

            return;
        }

        if (pInvestorPosition != null){
        	System.out.printf("instrumentid[%s]side[%c]frozenmargin[%f]\n", pInvestorPosition.getInstrumentID(),pInvestorPosition.getPosiDirection(),pInvestorPosition.getFrozenMargin());
        	System.out.printf("frozencash[%f]frozencommision[%f]", pInvestorPosition.getFrozenCash(),pInvestorPosition.getFrozenCommission());
            Position positiondata = new Position();
            positiondata.instrumentId = pInvestorPosition.getInstrumentID();
            positiondata.exchangeId = pInvestorPosition.getExchangeID();
            positiondata.direction = pInvestorPosition.getPosiDirection();
            
            int i = Integer.valueOf(Character.toString(pInvestorPosition.getPosiDirection()));
            String temp_side = "";
            if (i == 2) {temp_side = "Buy";}
            	else if(i == 3) {temp_side = "Sell";}
            String temp = pInvestorPosition.getInstrumentID()+"_"+ temp_side;
            
            if (pInvestorPosition.getExchangeID().trim().equals("SHFE")) {
            	
            	if (positionlist.containsKey(temp) == true && pInvestorPosition.getYdPosition() != 0 ) {
            		
            		positiondata.ydVolume = pInvestorPosition.getPosition();
            		positiondata.ydVolumeAval = positiondata.ydVolume - positionlist.get(temp).ydVolumeNotAval;
            		positiondata.ydVolumeNotAval = positionlist.get(temp).ydVolumeNotAval;
            		positiondata.todayVolume = positionlist.get(temp).todayVolume;
            		positiondata.todayVolumeAval = positionlist.get(temp).todayVolumeAval;
            		positiondata.todayVolumeNotAval = positionlist.get(temp).todayVolumeNotAval;
            		positiondata.totalVolume = positiondata.ydVolume + positiondata.todayVolume;
            		positiondata.totalVolumeAval = positiondata.ydVolumeAval + positiondata.todayVolumeAval;
            		positiondata.totalVolumeNotAval = positiondata.ydVolumeNotAval + positiondata.todayVolumeNotAval;
            		
            	}else if (positionlist.containsKey(temp) == true && pInvestorPosition.getYdPosition() == 0 ) {
            		
            		positiondata.ydVolume = positionlist.get(temp).ydVolume;
            		positiondata.ydVolumeAval = positionlist.get(temp).ydVolumeAval;
            		positiondata.ydVolumeNotAval = positionlist.get(temp).ydVolumeNotAval;
            		positiondata.todayVolume = pInvestorPosition.getPosition();
            		positiondata.todayVolumeAval = positiondata.todayVolume - positionlist.get(temp).todayVolumeNotAval;
            		positiondata.todayVolumeNotAval = positionlist.get(temp).todayVolumeNotAval;
            		positiondata.totalVolume = positiondata.ydVolume + positiondata.todayVolume;
            		positiondata.totalVolumeAval = positiondata.ydVolumeAval + positiondata.todayVolumeAval;
            		positiondata.totalVolumeNotAval = positiondata.ydVolumeNotAval + positiondata.todayVolumeNotAval;
            		
            	}else if (positionlist.containsKey(temp) == false && pInvestorPosition.getYdPosition() != 0) {
            		
            		positiondata.ydVolume = pInvestorPosition.getPosition();
            		positiondata.ydVolumeAval = positiondata.ydVolume;
            		positiondata.ydVolumeNotAval = 0;
            		positiondata.todayVolume = 0;
            		positiondata.todayVolumeAval = 0;
            		positiondata.todayVolumeNotAval = 0;
            		positiondata.totalVolume = positiondata.ydVolume + positiondata.todayVolume;
            		positiondata.totalVolumeAval = positiondata.ydVolumeAval + positiondata.todayVolumeAval;
            		positiondata.totalVolumeNotAval = 0;
            		
            	}else if (positionlist.containsKey(temp) == false && pInvestorPosition.getYdPosition() == 0) {
            		
            		positiondata.ydVolume = 0;
            		positiondata.ydVolumeAval = 0;
            		positiondata.ydVolumeNotAval = 0;
            		positiondata.todayVolume = pInvestorPosition.getPosition();
            		positiondata.todayVolumeAval = positiondata.todayVolume;
            		positiondata.todayVolumeNotAval = 0;
            		positiondata.totalVolume = positiondata.todayVolume;
            		positiondata.todayVolumeAval = positiondata.ydVolumeAval + positiondata.todayVolumeAval;
            		positiondata.totalVolumeNotAval = 0;
            	}
            	
            	
            }else {
            	
            	if(positionlist.containsKey(temp) == false) {
            		
            		positiondata.totalVolume = pInvestorPosition.getPosition();
                    positiondata.todayVolume = pInvestorPosition.getTodayPosition();
                	positiondata.ydVolume = pInvestorPosition.getPosition() - pInvestorPosition.getTodayPosition();
                	
                	positiondata.totalVolumeAval = positiondata.totalVolume;
                	positiondata.ydVolumeAval = positiondata.ydVolume;
                	positiondata.todayVolumeAval = positiondata.todayVolume;	
                	
                	positiondata.totalVolumeNotAval = 0;
                	positiondata.ydVolumeNotAval = 0;
                	positiondata.todayVolumeNotAval = 0;
                }else {
                	
                	positiondata.totalVolume = pInvestorPosition.getPosition();
                	positiondata.todayVolume = pInvestorPosition.getTodayPosition();
                	positiondata.ydVolume = pInvestorPosition.getPosition() - pInvestorPosition.getTodayPosition();
                	
                	positiondata.todayVolumeAval = positiondata.todayVolume - positionlist.get(temp).todayVolumeNotAval;
                	positiondata.ydVolumeAval = positiondata.ydVolume - positionlist.get(temp).ydVolumeNotAval;
                	positiondata.totalVolumeAval = positiondata.todayVolumeAval + positiondata.ydVolumeAval;
                	
                	positiondata.totalVolumeNotAval = positionlist.get(temp).totalVolumeNotAval;
                	positiondata.ydVolumeNotAval = positionlist.get(temp).ydVolumeNotAval;
                	positiondata.todayVolumeNotAval = positionlist.get(temp).todayVolumeNotAval;
            	}
            	            	
            }
            
            positionlist.put(temp, positiondata);            	
            base.onposition(positiondata);       
        }
        else
		{
			System.out.printf("NULL obj\n");
		}

    }

    public void OnRspQryInvestorPositionDetail(CThostFtdcInvestorPositionDetailField pInvestorPositionDetail, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

        if (pRspInfo != null && pRspInfo.getErrorID() !=0){

            System.out.printf("OnRspQryInvestorPositionDetail ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
        
            return;
        }

        if (pInvestorPositionDetail != null){
            
            System.out.println("Investor Position Detail Info Received:");
            System.out.printf("instrumentID[%s]exchangeId[%s]investorId[%s]direction[%c]volume[%d]openPrice[%f]openDate[%s]\n", 
            pInvestorPositionDetail.getInstrumentID(), pInvestorPositionDetail.getExchangeID(),pInvestorPositionDetail.getInvestorID(),
            pInvestorPositionDetail.getDirection(),pInvestorPositionDetail.getVolume(),pInvestorPositionDetail.getOpenPrice(),
            pInvestorPositionDetail.getOpenDate());
            
            //on_position_detail
        }
        else
		{
			System.out.printf("Investor Position Detail Info : Null\n");
		}

    }

    public void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo,int nRequestID, boolean bIsLast){
    	
    	System.out.println("method called");
        if (pRspInfo != null && pRspInfo.getErrorID() != 0){

            System.out.printf("OnRspQryInstrument ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
            
            return;
        }

        if (pInstrument != null){

            System.out.println("Instrument Info Received:");
            System.out.printf("instrumentId[%s]exchangeId[%s]instrumentName[%s]volumeMultiple[%d]priceTick[%f]\n",
                    pInstrument.getInstrumentID(), pInstrument.getExchangeID(),pInstrument.getInstrumentName(),
                    pInstrument.getVolumeMultiple(),pInstrument.getPriceTick());
            instrumentAndExchange.put(pInstrument.getInstrumentID(), pInstrument.getExchangeID());
            
//            Contract contractdata = new Contract();
//            contractdata.instrumentId = pInstrument.getInstrumentID();
//            contractdata.exchangeId = pInstrument.getExchangeID();
//            contractdata.instrumentName = pInstrument.getInstrumentName();
//            contractdata.productClass = pInstrument.getProductClass();
//            contractdata.volumeMultiple = pInstrument.getVolumeMultiple();
//            contractdata.priceTick = pInstrument.getPriceTick();
//            
//            base.oncontract(contractdata);
            	
        }
        else
		{
			System.out.println("Instrument Info :Null");
		}

    }
    
    public void OnRspQryOrder(CThostFtdcOrderField pOrder, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
    	
    	if (pRspInfo != null && pRspInfo.getErrorID() != 0) {
    		
    		System.out.printf("OnRspQryOrder ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
    		
    		return;
    	}
    	
    	if (pOrder != null){
    		
    		Order recieved = new Order();
    	    recieved.orderSysId = pOrder.getOrderSysID();
    	    recieved.brokerId = pOrder.getBrokerID();
    	    recieved.investorId = pOrder.getInvestorID();
    	    recieved.exchangeId = pOrder.getExchangeID();
    	    recieved.instrumentId = pOrder.getInstrumentID();
    	    recieved.orderRef = pOrder.getOrderRef();
    	    recieved.frontId = this.front_id;
    	    recieved.sessionId = this.session_id;
    	    recieved.volume = pOrder.getVolumeTotalOriginal();
        	recieved.tradedVolume = pOrder.getVolumeTraded();
        	recieved.price = pOrder.getLimitPrice();
        	recieved.status = pOrder.getOrderStatus();
        		
        	if (pOrder.getOrderStatus() == '3') {
        		orderlist.put(recieved.orderSysId, recieved);

        		String side;
        		side = pOrder.getDirection()=='2'?"Buy":"Sell";
        		
        		String offset = pOrder.getCombOffsetFlag();

        		
        		if (offset.equals("3")) {
        			
        			offset = "Td";
        			positionbuffer.put(pOrder.getInstrumentID()+"_"+side + "_" + offset, pOrder.getVolumeTotalOriginal());

        			return;
        		}
        		else if (offset.equals("1")) {
        			
        			offset = "Yd";
        			positionbuffer.put(pOrder.getInstrumentID()+"_"+side + "_" + offset, pOrder.getVolumeTotalOriginal());

        			return;
        		}
        		
        		positionbuffer.put(pOrder.getInstrumentID()+"_"+side, pOrder.getVolumeTotalOriginal());
        	}
   	

        }
        else
		{
			System.out.printf("没有未成交或部分成交单\n");
		}
    	
    	
    }

    public void OnRspOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

        if (pRspInfo != null && pRspInfo.getErrorID() != 0){

            System.out.printf("OnRspOrderInsert ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(),pRspInfo.getErrorMsg());
            
            return;
        }

        if (pInputOrder != null){

            System.out.println("Order Insert Info Received:");
            System.out.printf("InstrumentName[%s]VolumeCondition[%c]StopPrice[%f]LimitPrice[%s]\n",
                    pInputOrder.getInstrumentID(), pInputOrder.getVolumeCondition(),pInputOrder.getStopPrice(),
                    pInputOrder.getLimitPrice());
            
            
        }
        else
		{
			System.out.printf("NULL obj\n");
		}

    }

    public void OnRtnOrder(CThostFtdcOrderField pOrder){

//    	System.out.printf("OnRtnOrder OrderRef[%s]OrderStatus[%c]FrontID[%d]SessionID[%d]SysID[%s]Exchange[%s]\n"
//    			,pOrder.getOrderRef(),pOrder.getOrderStatus(),pOrder.getFrontID(),pOrder.getSessionID(),pOrder.getOrderSysID(),pOrder.getExchangeID()
//    			);
    	if (pOrder.getOrderStatus() == '3' || pOrder.getOrderStatus() == 'a') {
    		String side;
    		side = pOrder.getDirection() == '2'?"Buy":"Sell";
    		positionbuffer.put(pOrder.getInstrumentID().trim()+"_"+side, pOrder.getVolumeTotal());
    	}
    	if (pOrder.getOrderStatus() == '5')	{
    		String side;
    		side = pOrder.getDirection() == '2'?"Buy":"Sell";
    		if (positionbuffer.containsKey(pOrder.getInstrumentID().trim()+"_"+side) == true) {
    			positionbuffer.remove(pOrder.getInstrumentID().trim()+"_"+side);
    		}
    	}
    	
    	if (orderlist.containsKey(pOrder.getOrderRef().trim())) {
    		if (orderlist.get(pOrder.getOrderRef().trim()).tradedVolume != pOrder.getVolumeTraded()) {
    			Trade tradedata = new Trade();
            	
            	tradedata.orderref = pOrder.getOrderRef();
            	tradedata.instrumentId = pOrder.getInstrumentID();
            	tradedata.exchangeId = pOrder.getExchangeID();
            	tradedata.orderSysId = pOrder.getOrderSysID();
            	tradedata.volume = pOrder.getVolumeTraded() - orderlist.get(pOrder.getOrderRef()).tradedVolume;
            	tradedata.price = pOrder.getLimitPrice();
            	System.out.println("success");
            	base.ontrade(tradedata);
    		}
    	}
//    	if (orderlist.get(pOrder.getOrderRef().trim()).tradedVolume != pOrder.getVolumeTraded()) {
//    		
//    		Trade tradedata = new Trade();
//        	
//        	tradedata.orderref = pOrder.getOrderRef();
//        	tradedata.instrumentId = pOrder.getInstrumentID();
//        	tradedata.exchangeId = pOrder.getExchangeID();
//        	tradedata.orderSysId = pOrder.getOrderSysID();
//        	tradedata.volume = pOrder.getVolumeTraded() - orderlist.get(pOrder.getOrderRef()).tradedVolume;
//        	tradedata.price = pOrder.getLimitPrice();
//        	
//        	base.ontrade(tradedata);
//    	}
    	
	    Order recieved = new Order();
	    recieved.orderSysId = pOrder.getOrderSysID();
	    recieved.brokerId = pOrder.getBrokerID();
	    recieved.investorId = pOrder.getInvestorID();
	    recieved.exchangeId = pOrder.getExchangeID();
	    recieved.instrumentId = pOrder.getInstrumentID();
	    recieved.orderRef = pOrder.getOrderRef().trim();
	    recieved.frontId = this.front_id;
	    recieved.sessionId = this.session_id;
	    recieved.volume = pOrder.getVolumeTotalOriginal();
    	recieved.tradedVolume = pOrder.getVolumeTraded();
    	recieved.price = pOrder.getLimitPrice();
    	recieved.status = pOrder.getOrderStatus();
    	

    	
    	orderlist.put(recieved.orderRef, recieved);
    	base.onorder(recieved);

    }

//    public void OnRtnTrade(CThostFtdcTradeField pTrade){
//    	System.out.printf("OnRtnTrade Order with orderref[%s] at exchange[%s] has traded volume[%d] at price[%f]\n",
//    	pTrade.getOrderSysID(), pTrade.getExchangeID(), pTrade.getVolume(), pTrade.getPrice());
//    	
//    	Trade tradedata = new Trade();
//    	
//    	tradedata.orderref = pTrade.getOrderRef();
//    	tradedata.instrumentId = pTrade.getInstrumentID();
//    	tradedata.exchangeId = pTrade.getExchangeID();
//    	tradedata.orderSysId = pTrade.getOrderSysID();
//    	tradedata.tradeId = pTrade.getTradeID();
//    	tradedata.direction = pTrade.getDirection();
//    	tradedata.offset = pTrade.getOffsetFlag();
//    	tradedata.price = pTrade.getPrice();
//    	tradedata.volume = pTrade.getVolume();
//    	tradedata.time = pTrade.getTradeTime();
//    	
//
//    	base.ontrade(tradedata);
//    	
//    }
    
    public void OnErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField pRspInfo){

        System.out.printf("OnErrRtnOrderInsert Orderref[%s] ErrorID[%d] ErrMsg[%s]\\n",pInputOrder.getOrderRef(), pRspInfo.getErrorID(), pRspInfo.getErrorMsg());

    }

    

    public void OnRspOrderAction(CThostFtdcInputOrderActionField pInputOrderAction, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

        if (pRspInfo != null && pRspInfo.getErrorID() != 0){

            System.out.printf("OnRspOrderAction ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
            System.out.println("Order Cancel Error");
        
            return;
        }

        else{
        	
            System.out.printf("Order canceled with ref id [%s], removed from orderlist",pInputOrderAction.getOrderRef());
            
            Order removedOrder = new Order();
            removedOrder.orderSysId = pInputOrderAction.getOrderSysID();
            removedOrder.brokerId = pInputOrderAction.getBrokerID();
            removedOrder.investorId = pInputOrderAction.getInvestorID();
            removedOrder.exchangeId = pInputOrderAction.getExchangeID();
            removedOrder.instrumentId = pInputOrderAction.getInstrumentID();
            removedOrder.orderRef = pInputOrderAction.getOrderRef();
            removedOrder.frontId = this.front_id;
            removedOrder.sessionId = this.session_id;
            removedOrder.price = pInputOrderAction.getLimitPrice();
            
            base.oncancel(removedOrder);
            orderlist.remove(pInputOrderAction.getOrderRef());
        }


    }

    public void OnRspQrySettlementInfo(CThostFtdcSettlementInfoField pSettlementInfo, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
    	
    	if (pRspInfo != null && pRspInfo.getErrorID() != 0){

            System.out.printf("OnRspQrySettlementInfo ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
            System.out.println("SettlementInfo Qry Error!!");
            
            return;
        }
    	
//    	else{
//            System.out.println("SettlementInfo Qryed\n");
//        }
    }
    
    public void OnRspSettlementInfoConfirm(CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

        if (pRspInfo != null && pRspInfo.getErrorID() != 0){

            System.out.printf("OnRspSettlementInfoConfirm ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
            System.out.println("Settlement Info Confirm Error!!");
            
            return;
        }

        else{
            System.out.println("Settlement Info confirmed, settlementID is " + pSettlementInfoConfirm.getSettlementID() +" on " +pSettlementInfoConfirm.getConfirmDate());
        }

    }

    public void setInstruementids(String s) {
        this.instruementids = s;
    }

    public void setT_BrokerId(String s) {
        this.t_BrokerId = s;
    }

    public void setT_UserId(String s) {
        this.t_UserId = s;
    }

    public void setT_PassWord(String s) {
        this.t_PassWord = s;
    }




    
}


