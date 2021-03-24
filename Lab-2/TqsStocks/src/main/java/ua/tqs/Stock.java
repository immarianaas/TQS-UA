package ua.tqs;

public class Stock {
    private String name;
    private int quantity;

    public Stock(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public void setName(String newname) { name = newname; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int newquant) { quantity = newquant; }
    
}
