Twilio operates on a prepaid model. 

Phone numbers incur a monthly rental fee (USD $6.5 per month).

SMS messages are billed per message (from the prepaid balance). 

International SMS may cost more. 

Trial accounts are restricted to verified recipient numbers only.

My Twilio number is an Australian number.

I saved my real Australian number in the database for testing.

Also, the tested user need to have these manually set values in the Users table:
- using_mfa = 1 (when it's 0, then user should be able to log in with just email and password fine)
- enabled = 1
- non_locked = 1

Currently, the Twilio related env settings are in application-dev.yml only.

I've used the wise USD account on the account. 

Currently, I've turned off auto-recharge and kept some balance left.

In theory it should continue charging monthly rental for my twilio phone number from that balance.