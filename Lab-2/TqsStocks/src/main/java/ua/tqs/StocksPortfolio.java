package ua.tqs;

import java.util.ArrayList;

public class StocksPortfolio {

    public String name;
    public IStockMarket service;
    public ArrayList<Stock> portfolio = new ArrayList<Stock>();

    public StocksPortfolio(IStockMarket market) { service = market; }

    public IStockMarket getMarketService() { 
        return service; 
    }

    public void setMarketService(IStockMarket ism) {
        service = ism;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getTotalValue() { 
        // no pdf nao diz mas eu suponho q seja 
        // suposto multiplicar pela quantidade..?
        double total = 0;
        for (Stock s : portfolio) {
            total += service.getPrice(s.getName())*s.getQuantity();
        }
        return total;
    }
    
    public void addStock(Stock stock) { portfolio.add(stock); }


}
