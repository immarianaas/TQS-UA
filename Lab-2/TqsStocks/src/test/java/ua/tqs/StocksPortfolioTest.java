package ua.tqs;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.doubleThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.is;



@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {
    
    // 1. prepare a mock to substitute the remote service (@Mock annotation)
    @Mock
    IStockMarket service;

    // 2. create an instance of the subject under test
    @InjectMocks
    StocksPortfolio portfolio;

    @Test
    public void getTotalValueTest() {

        //IStockMarket service = mock(IStockMarket.class);
        //StocksPortfolio portfolio = new StocksPortfolio( service );

//      3. load the mock with the proper expectations (when.. thenReturn)
        when(service.getPrice("EBAY")).thenReturn(4.0);
        when(service.getPrice("MSFT")).thenReturn(1.5);

//      4. execute the test
        portfolio.addStock(new Stock("EBAY", 2));
        portfolio.addStock(new Stock("MSFT", 4));
        double result = portfolio.getTotalValue();

//      5. verify the result and the use of the mock
        double expectedRes = 4.0*2 + 1.5*4;
        // assertEquals(expectedRes, result);
        assertThat(expectedRes, is(result));
        verify(service, times(2)).getPrice( anyString() );

    }
}
