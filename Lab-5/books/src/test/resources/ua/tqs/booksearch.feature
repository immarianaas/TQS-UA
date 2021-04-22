Feature: Book search
    To allow a customer to find his favourite books quickly, the library must offer multiple ways to search for a book

    Scenario: Search books by publication year
        Given a book with the title 'One good book', written by 'Anonymous', published in 14-04-2013
            And another book with the title 'Some other book', written by 'Tim Tomson', published in 23-08-2014
            And another book with the title 'How to cook a dino', written by 'Fred Flintstone', published in 01-01-2012
        When the customer searches for books published between 2013 and 2014
            Then 2 books should have been found
                And Book 1 should have the title 'Some other book'
                And Book 2 should have the title 'One good book'

    Scenario: Search books by author
        Given a book with the title 'Zacht Tikt De Regen', written by 'Ferry de Lits', published in 03-10-2006 
            And another book with the title 'Geef Mij Je Angst', written by 'Andre Hazes', published in 05-03-1983
            And another book with the title 'Bedankt Mijn Vriend', written by 'Andre Hazes', published in 01-12-1988
        When the customer serches for books written by the author 'Andre Hazes'
            Then 2 books should have been found
                And Book 1 should have the title 'Bedankt Mijn Vriend'
                And Book 2 should have the title 'Geef Mij Je Angst'
