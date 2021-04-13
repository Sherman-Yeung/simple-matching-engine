# Take-home Exercise: Senior Exchange Software Engineer

This exercise is designed to help us understand how you think and approach your work. While the code should work, we are also looking for readability, testing and efficiency. If you need to make an incomplete submission, we ask that you provide detailed descriptions of what you would have done given ample time.

Please fork this repository to your personal Github account and provide a link to your fork as your submission along with instructions on how to run the program and tests. If the repository is private, please invite:  `hoi`

## Brief
This exercise is to write a simple matching engine. The program should read from two input files that you can find in `src/main/resources`:
- symbols
- orders

A symbol may be halted, in which case, do not accept any orders for that symbol.

For symbols that are not halted, order must have all required fields for the order to be accepted.

An order can be one of two sides:  
- buy
- sell

There are two types of orders:
- Limit orders will try to execute at or better than the price defined (lower is better for buyers and higher is better for sellers). All fields are required. If the order does not match, it will sit on the order book waiting to be matched.
- Market orders will execute at any price. All fields except price is required. If a price is set, please ignore it. If the order does not match, it will be rejected with the reason that there was no match.

For simplicity, assume all orders are for 1 share, which means, if there is a match, orders will match with exactly 1 other order of the same symbol and with the best price. Best price means lowest price for buyers and highest price for sellers.

If an order matches with another order, a trade is made. A trade consists of: symbol, price, timestamp

Produce 3 output files:
- trades.txt - contains all trades that were made
- rejected.txt - contains all rejected orders and their rejected reasons
- orderbook.txt - contains all orders that were not traded by the end

## Rubric
Criteria | Weight
---------|--------
Readability | 20%
Testing | 50%
Code Organization | 10%
OO-patterns, Data Structures and Algorithm | 15%
Completeness | 5%

## Other Notes
Feel free to reach out with any questions or clarifications, but particularly over the weekend or evening we may not be able to respond. If you do not hear from us, you can make any assumptions that seem reasonable and simply document them in the README.

