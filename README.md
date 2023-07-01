# Tradeus: A Stock Tracker App

## Introduction
Tradeus is a stock tracker app, with features that distinguishes it from other typical stock tracker apps. 

It is built with Angular(frontend), Spring Boot (backend) with SQL, Redis and MongoDB as databases.

Some exciting implementations in Tradeus include charting, ability to create personal portfolios which displays real time update of stock profits calculated using both annualised and total profits. 

### Technology
- Angular *Typescript*
- Spring Boot *Java*
- Chart JS *Javascript*
- ngBootStrap and Bootstrap 
- Java Mail
- Web Sockets

### Using Tradeus


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
