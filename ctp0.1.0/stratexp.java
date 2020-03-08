

public class stratexp implements basestrat {
	public TdspiImpl temp;
	
	public stratexp(TdspiImpl input) {
		temp = input;
	}

//	@Override
//	public void register(TdspiImpl input) {
//		// TODO Auto-generated method stub
//		temp = input;
//	}
	
	@Override
	public void ontrade(Trade tradedata) {
		// TODO Auto-generated method stub
		System.out.println("ontrade");
	}

	@Override
	public void ontick() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onaccount(Account accountdata) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void oncontract(Contract contractdata) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onorder(Order recieved) {
		// TODO Auto-generated method stub
		System.out.println("onorder");
	}

	@Override
	public void onposition(Position positiondata) { 
		// TODO Auto-generated method stub
		System.out.println(positiondata.instrumentId + positiondata.direction + "  total  " + positiondata.totalVolume + "  td  " + positiondata.todayVolume + "  yd  " + positiondata.ydVolume + "  total aval  " + positiondata.totalVolumeAval + "  td aval  " + positiondata.todayVolumeAval + "  yd aval  " + positiondata.ydVolumeAval + "  totalnot  " + positiondata.totalVolumeNotAval + "  tdnot  " + positiondata.todayVolumeNotAval + "  ydnot  " + positiondata.ydVolumeNotAval);
		
		
	}

	@Override
	public void onstart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onstop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void oncancel(Order removedOrder) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
