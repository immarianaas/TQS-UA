package ua.tqs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookSearchSteps {
    Library lib = new Library();
    List<Book> books_found = new ArrayList<Book>();
    

    @ParameterType("([0-9]{2})-([0-9]{2})-([0-9]{4})")
    public Date iso8601Date(String day, String month, String year) {
        LocalDateTime a = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), 0, 0);
        return java.sql.Timestamp.valueOf(a);
    }


    /*
    @Given("a book with the title {string}, written by {string}, published in {iso8601Date}")
    public void a_book_with_the_title_written_by_published(String string, String string2, Date date) {
        // Write code here that turns the phrase above into concrete actions
        System.err.println("HMMMMMM");
    }
    */

    @Given("a(nother) book with the title {string}, written by {string}, published in {iso8601Date}")
    public void a_book_with_the_title_written_by_published_in(String title, String author, Date date) {
        Book book = new Book(title, author, date);
        lib.addBook(book);
    }
    @When("the customer searches for books published between {int} and {int}")
    public void the_customer_searches_for_books_published_between_and(Integer int1, Integer int2) {
       
        Date bef = iso8601Date("01", "01", String.valueOf(int1));
        Date aft = iso8601Date("31", "12", String.valueOf(int2));

        books_found = lib.findBooks(bef, aft);
    }
    @Then("{int} books should have been found")
    public void books_should_have_been_found(Integer int1) {
        assertThat(int1, equalTo(books_found.size()));
    }

    @Then("Book {int} should have the title {string}")
    public void book_should_have_the_title(Integer int1, String string) throws Exception{
        assertTrue(books_found.size() >= int1);
        assertTrue(books_found.get(int1-1) != null);
        assertThat(string, equalTo(books_found.get(int1-1).getTitle()));

    }
}
