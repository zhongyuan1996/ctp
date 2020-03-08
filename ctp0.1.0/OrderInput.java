 public class OrderInput{

    long order_id;

    String instrument_id;
    String exchange_id;
    String account_id;
    String client_id;
    String broker_id;
    
    String instrument_type;

    double limit_price;
    double frozen_price;

    int volume;

    String side;
    String offset;
    String price_type;
    String volume_condition;
    String time_condition;

    long parent_id;

    public void set_order_id(long input){
        this.order_id = input;
    }
    public void set_broker_id(String input){
        this.broker_id = input;
    }
    public void set_instrument_id(String input){
        this.instrument_id = input;
    }
    public void set_exchange_id(String input){
        this.exchange_id = input;
    }
    public void set_account_id(String input){
        this.account_id = input;
    }
    public void set_client_id(String input){
        this.client_id = input;
    }
    public void set_instrument_type(String input){
        this.instrument_type = input;
    }
    public void set_limit_price(double input){
        this.limit_price = input;
    }
    public void set_frozen_price(double input){
        this.frozen_price = input;
    }
    public void set_volume(int input){
        this.volume = input;
    }
    public void set_side(String input){
        this.side = input;
    }
    public void set_offset(String input){
        this.offset = input;
    }
    public void set_price_type(String input){
        this.price_type = input;
    }
    public void set_volume_condition(String input){
        this.volume_condition = input;
    }
    public void set_time_condition(String input){
        this.time_condition = input;
    }
    public void set_parent_id(long input){
        this.parent_id = input;
    }

    public String get_instrument_id(){
        return (instrument_id);
    }

    public String get_exchange_id(){
        return (exchange_id);
    }

    public String get_account_id(){
        return (account_id);
    }

    public String get_client_id(){
        return (client_id);
    }
}