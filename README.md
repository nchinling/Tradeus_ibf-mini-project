# Tradeus: A Stock Tracker App

## Introduction
Tradeus is a stock tracker app, with features that distinguishes it from other typical stock tracker apps. 

Some exciting implementations in Tradeus include charting, creation of watch list and ability to create personal portfolios which displays real time update of stock profits calculated using both annualised and total profits. 

It is built with Angular(frontend), Spring Boot (backend) with SQL, Redis and MongoDB as databases. More details are available at my portfolio project site https://ngchinling.com/projects/stocktracker.html

### Technology
- Angular *Typescript*
- Spring Boot *Java*
- Chart JS *Javascript*
- ngBootStrap and Bootstrap 
- Java Mail
- Web Sockets
- Stripe E-payment

### Using Tradeus


### Features
- Email: The app sends an email notification to the user when there is a new  sign-up or when the user logs in. (Try it with an authentic email address)
- Web Socket: Used in getting live data from external financial data firm. See feature in 'live data' tab of dashboard page


### Progress Log
15 June (Thu)
- saved US stocks list in mongodb
- created StockInfo class
- implemented Stock List filter
- implemented link from stock in list to stock quote when clicked
- caching of stock info in redis using concatenation of stock symbol + interval as key

16 June (Fri)
- fixed bugs
- beautified rows
- implemented watchlist database
- implemented button message for added symbols

17 June (Sat)
- implemented company profile and logo

18 June (Sun)
- Responsive app
- Working accordion
- Beautified app
- watchlist entry deletion

19 June (Mon)
- implemented link from watchlist
- allow for user profile edit

20 June (Tue)
- refactored user profile edit
- implemented working charts. Nice :)

21 June (Wed)
- improved chart functioning and beautified
- started on annualised return portfolio

22 June (Thu)
- implemented delete for portfolio

23 June (Fri)
- improved sql table

24 June (Sat)
- implemented delete for portfolio
25 June (Sun)
- used nav tabs
- implemented delete for portfolio

26 June (Mon)
- removed bugs for portfolio

27 June (Tues)
- implemented web socket connection

28 June (Wed)
- improved web socket connection
- improved presentation of live data
- changed chart title to stock name instead of stock symbol

29 June (Thu)
- websocket counter

30 June (Thu)
- removed websocket counter
- implemented backend websocket with external financial data api
- implemented websocket between angular client and spring
- implemented email notification on login
- improved css design for delete button

1 Jul (Sat)
- favicon
- improved client-side validation requirements using regex and min length
- added email notification to new registration
- created spinner

3 Jul (Mon)
- integrated e-payment (Stripe)

4 Jul (Tue)
- integrated Stripe successfully
- created successful payment and cancellation page 
- implemented Manifest
- 'Getting Started' page

5 Jul (Wed)
- Provide validation for 'Edit user' form, portfolio form and chart fields
- Create 'Learn' and 'About Us' pages
- start deployment
- create corporate Gmail

6 Jul (Thu)
- Sorted single server deployment for sockets and stripe

7 Jul (Fri)
- fixed single server deployment payment success and payment cancellation

8 Jul (Sat)
- Provide minimum amount for credit card
- Started deployment on Redis
- purchased domain name

10 Jul (Mon)
- fixed deployed chart title bug

13 Jul (Thu)
- improved responsiveness
