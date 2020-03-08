public class OrderAction{
	
	String broker_id;
	String investor_id;
	String order_ref;
    String instrument_id;
    String sys_id;
    String exchange_id;
    
    int front_id;
    int session_id;
    
    public void set_exchange_id(String input){
        this.exchange_id = input;
    }   
    public void set_sys_id(String input){
        this.sys_id = input;
    }   
    public void set_broker_id(String input){
        this.broker_id = input;
    }
    public void set_investor_id(String input){
        this.investor_id = input;
    }
    public void set_instrument_id(String input){
        this.instrument_id = input;
    }

    public void set_order_ref(String input){
        this.order_ref = input;
    }

    public void set_front_id(int input){
        this.front_id = input;
    }

    public void set_session_id(int input){
        this.session_id = input;
    }

    public String get_instrument_id(){
        return instrument_id;
    }

    public String get_order_ref(){
        return order_ref;
    }

    public int get_front_id(){
        return front_id;
    }

    public int get_session_id(){
        return session_id;
    }

}