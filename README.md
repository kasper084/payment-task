# payment-service 
#host:
localhost:9007
#endpoints:
POST /payment/new 
json for testing :
GET /payment/:id
GET /payments?currency=X
GET /payments/stats?currency=X
#json for testing payment/new:
{
"fiatAmount": 700,
"fiatCurrency": "EUR",
"coinCurrency": "BTC"
}
