import ctp.thosttraderapi.*;


public class TestMain{
    static{
        // System.out.println("java.library.path:"+System.getProperty("java.library.path"));
        System.loadLibrary("thostmduserapi_se");
        System.loadLibrary("thosttraderapi_se");
        System.loadLibrary("thosttraderapi_wrap");
    }

    public static void main(String[] args) throws Exception{
    	
//    	CThostFtdcMdApi mdApi = CThostFtdcMdApi.CreateFtdcMdApi();
//    	MdspiImpl pMdspiImpl = new MdspiImpl(mdApi);
//    	
//    	pMdspiImpl.setM_BrokerId("9999");
//    	pMdspiImpl.setM_UserId("144091");
//    	pMdspiImpl.setM_PassWord("zhong6yuan20");
    	
    	

        CThostFtdcTraderApi tdApi = CThostFtdcTraderApi.CreateFtdcTraderApi();
        TdspiImpl pTdspiImpl = new TdspiImpl(tdApi);
        
        pTdspiImpl.setT_BrokerId("9999");
        pTdspiImpl.setT_UserId("144091");
        pTdspiImpl.setT_PassWord("zhong6yuan20");
        
        CThostFtdcQrySettlementInfoField req1 = new CThostFtdcQrySettlementInfoField();
        req1.setBrokerID("9999");
        req1.setInvestorID("144091");
        CThostFtdcSettlementInfoConfirmField req2 = new CThostFtdcSettlementInfoConfirmField();
        req2.setBrokerID("9999");
        req2.setInvestorID("144091");
        
//        mdApi.RegisterSpi(pMdspiImpl);
//        mdApi.RegisterFront("tcp://218.202.237.33 :10012");
//        mdApi.Init();
        
        tdApi.RegisterSpi(pTdspiImpl);
        tdApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESUME);
        tdApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESUME);
        tdApi.RegisterFront("tcp://180.168.146.187:10101");

        tdApi.Init();
        
        Thread.sleep(1000);
        
        tdApi.ReqQrySettlementInfo(req1, 1);
        
        Thread.sleep(1000);
        
        tdApi.ReqSettlementInfoConfirm(req2, 1);

        Thread.sleep(1000);
        pTdspiImpl.reqQryInstrumentByExchangeID("CFFEX");
        Thread.sleep(1000);
        pTdspiImpl.reqQryInstrumentByExchangeID("CZCE");
        Thread.sleep(1000);
        pTdspiImpl.reqQryInstrumentByExchangeID("DCE");
        Thread.sleep(1000);
        pTdspiImpl.reqQryInstrumentByExchangeID("INE");
        Thread.sleep(1000);
        pTdspiImpl.reqQryInstrumentByExchangeID("SHFE");
        Thread.sleep(2000);
        
        String ins = "AP910";
        double price = 10200.0;
        
        pTdspiImpl.reqPosition();
        
        Thread.sleep(2000);
        
        
        pTdspiImpl.reqQryOrder();
        

        


//        OrderInput myinput = new OrderInput();
//        
//        myinput.set_broker_id("9999");
//        myinput.set_account_id("144091");
//        myinput.set_instrument_id(ins);
//        myinput.set_side("SideBuy");
//        myinput.set_offset("OffsetOpen");
//        myinput.set_volume(7);
//        myinput.set_volume_condition("VolumeConditionAny");
//        myinput.set_time_condition("TimeConditionGFD");
//        myinput.set_price_type("PriceTypeLimit");
//        myinput.set_limit_price(price);
        


        
        while (!Thread.currentThread().isInterrupted()){

            Thread.sleep(10000);
        		if (0 > 1) {
        			break;
        			
        		}
        		
        	}
        Thread.sleep(10000);
        System.out.print("DCed");
        
        
        Thread.currentThread().interrupt();

        



    
    }
}