# ctp_v0.1.0 documentation

## Yuan Zhong



### TdspoImpl

#### Arguments:CThostFtdcTraderApi tdapi

#### Use:

初始化



### OnFrontConnected

#### Arguments:Void

### Use:

接入front时登陆



### OnFrontDisconnected

#### Arguments:int nReason

#### Use:

掉线的回调函数



### OnHeartBeatWarning

#### Arguments:int nTimeLapse

#### Use:

心跳监控



### reqQrySettlementInfo

#### Arguments:Void

#### Use:

查询结算结果

#### OnRspQrySettlementInfo

reqQruSettlementInfo的回调函数



### reqSettlementInfoConfirm

#### Arguments:Void

#### Use:

确认结算结果

#### OnRspSettlementInfoConfirm

reqSettlementInfoConfirm的回调函数



### reqAccount

#### Arguments:Void

#### Use:

查询账户信息

### OnRspQryTradingAccount

reqAccount的回调函数



### reqPosition

#### Arguments:Void

#### Use:

查询仓位信息

### OnRspQryInvestorPosition

reqPosition的回调函数,区分各个交易所,买卖和今仓昨仓.维护HashMap poitionlist



### reqPositionDetail

#### Arguments:Void

#### Use:

查询仓位具体信息

#### OnRspQryInvestorPositionDetail

reqPositionDetail的回调函数



### reqQryOrder

#### Arguments:Void

#### Use:

查询挂单信息

#### OnRspQryOrder

reqQryOrder的回调函数. 维护HashMap positionbuffer 进行锁仓.



### reqQryInstrumentByExchangeID

#### Arguments:String exchangeid

#### Use:

初始化HashMap instrumentAndExchange, 记录标的和对应的交易所

### OnRspQryInstrument

reqQryInstrument的回调函数



### reqQryInstrument

#### Arguments:Void

#### Use:

查询所有标的



### reqQryOneInstrument

#### Arguments:String instrumentID

#### Use:

查询指定标的



### OnRspUserLogin

#### Arguments:CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast

#### Use:

登陆的回调函数. 记录frontID和sessionID



### reqQryOrder

#### Arguments: Void

#### Use:

查询已挂单信息.

### onRspQryOrder

reqQryOrder 的回调, 使用HashMap positionbuffer 储存未成交或部分成交的已挂单信息.



### sendOrder

#### Arguments: OrderInput Input

#### Use:

基础的发单函数, 能通过orderinput 自定义所有的选项. to_ctp 函数是本函数的支持函数



### sendOrderBuyCloseToday,  sendOrderSellCloseToday, sendOrderBuyCloseYd,  sendOrdersellCloseYd

#### Arguments: String InstrumentId, **int** volume, **double** price

InstrumentId: a string of instrumentId

Volume: a int of volume of the given instrument

Price: the price of the instrument

#### Return: Boolean

#### Use:

基础的平今平昨, 只是作为AutoSplit的支持功能存在, 单独使用无法判断仓位所以不推荐使用



### sendOrderBuyOpen

#### Arguments: String InstrumentId, **int** volume, **double** price

InstrumentId: a string of instrumentId

Volume: a int of volume of the given instrument

Price: the price of the instrument

#### Return: Boolean

#### Use:

简化的发买单函数



### sendOrderBuySell

#### Arguments: String InstrumentId, **int** volume, **double** price

InstrumentId: a string of instrumentId

Volume: a int of volume of the given instrument

Price: the price of the instrument

#### Return: Boolean

#### Use:

简化的发卖单函数



### sendOrderBuyCloseTodayAutoSplit

#### Arguments: String InstrumentId, **int** volume, **double** price, **int** which

InstrumentId: a string of instrumentId

Volume: a int of volume of the given instrument

Price: the price of the instrument

Which: if which = 0, the program will offset today and offset yesterday then open. if which = 1, the program will offset today then open.

#### Return: Boolean

#### Use:

```sequence
Title:sendOrderBuyCloseTodayAutoSplit 自动平今买
offsetBuyToday->offsetBuyYesterday:平今后继续平昨(which = 0)
offsetBuyYesterday->Buyopen:如果还有多余订单,平昨后开仓
offsetBuyToday->Buyopen:平今后直接开仓(which = 1)

Note over offsetBuyToday:return
offsetBuyYesterday-->offsetBuyToday:return
Buyopen-->offsetBuyToday:return
```



### sendOrderSellCloseTodayAutoSplit

#### Arguments: String InstrumentId, **int** volume, **double** price, **int** which

InstrumentId: a string of instrumentId

Volume: a int of volume of the given instrument

Price: the price of the instrument

Which: if which = 0, the program will offset today and offset yesterday then open. if which = 1, the program will offset today then open.

#### Return: Boolean

#### Use:

```sequence
Title :sendOrderSellCloseTodayAutoSplit 自动平今卖
offsetSellToday->offsetSellYesterday:平今后继续平昨(which = 0)
offsetSellYesterday->Sellopen:如果还有多余订单,平昨后开仓
offsetSellToday->Sellopen:平今后直接开仓(which = 1)

Note over offsetSellToday:return
offsetSellYesterday-->offsetSellToday:return
Sellopen-->offsetSellToday:return
```



### sendOrderBuyCloseYdAutoSplit

#### Arguments: String InstrumentId, **int** volume, **double** price, **int** which

InstrumentId: a string of instrumentId

Volume: a int of volume of the given instrument

Price: the price of the instrument

Which: if which = 0, the program will offset yesterday and offset today then open. if which = 1, the program will offset yesterday then open.

#### Return: Boolean

#### Use:

```sequence
Title :sendOrderBuyCloseYdAutoSplit 自动平昨买
offsetBuyYesterday->offsetBuyToday:平昨后继续平今(which = 0)
offsetBuyToday->Buyopen:如果还有多余订单,平今后开仓
offsetBuyYesterday->Buyopen:平昨后直接开仓(which = 1)

Note over offsetBuyYesterday:return
offsetBuyToday-->offsetBuyYesterday:return
Buyopen-->offsetBuyYesterday:return
```



### sendOrderSellCloseYdAutoSplit

#### Arguments: String InstrumentId, **int** volume, **double** price, **int** which

InstrumentId: a string of instrumentId

Volume: a int of volume of the given instrument

Price: the price of the instrument

Which: if which = 0, the program will offset yesterday and offset today then open. if which = 1, the program will offset yesterday then open.

#### Return: Boolean

#### Use:

```sequence
Title :sendOrderSellCloseYdAutoSplit 自动平昨卖
offsetSellYesterday->offsetSellToday:平昨后继续平今(which = 0)
offsetSellToday->Sellopen:如果还有多余订单,平今后开仓
offsetSellYesterday->Sellopen:平昨后直接开仓(which = 1)

Note over offsetSellYesterday:return
offsetSellToday-->offsetSellYesterday:return
Sellopen-->offsetSellYesterday:return
```



### OnRspOrderInsert

各种sendorder的回调函数



### cancelOrder

#### Arguments:String orderRef

#### Use:

通过orderRef 撤单. 所有的挂单信息都已存在HashMap orderlist里



### cancelAllOrder

#### Arguments:Void

#### Use:

撤所有的未成交与部分成交单

### OnRspOrderAction

撤单的回调函数



