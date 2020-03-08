
public interface basestrat {
	
	void ontrade(Trade tradedata);
	
	void ontick();
	
	void onaccount(Account accountdata);
	
	void oncontract(Contract contractdata);

	void onorder(Order recieved);

//	void register(TdspiImpl input);

	void onposition(Position positiondata);
	
	void onstart();
	
	void onstop();
	
	void oncancel(Order removedOrder);
	
	
	
}
