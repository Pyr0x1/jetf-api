# jetf-api [![workflow](https://github.com/Pyr0x1/jetf-api/actions/workflows/build.yml/badge.svg)](https://github.com/Pyr0x1/jetf-api/actions/workflows/build.yml) [![License: LGPL v3](https://img.shields.io/badge/License-LGPL_v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
Unofficial Java API to retrieve data about ETFs from the JustETF website. The libray may stop working without notice if the web api changes, so use at your own risk.

## Usage

Here are some examples of the available api calls:

```java
// Retrieves general info about ETFs, performing a search based on a query string, limits by default to 25 results
SearchResponse response = new JustETF().search(Locale.ITALY, "VWCE");

// Retrieves general info about ETFs, performing a search based on a query string and limiting results with the parameters "start" and "length"
SearchResponse response = new JustETF().search(Locale.ITALY, "VWCE", 0, 10);

// Gets the quote info for the given ETF ISIN
Quote quote = new JustETF().getQuote("IE00BK5BQT80", Locale.ITALY);

// Convenience method that performs a search based on a query string including also quote info
SearchResponse response = new JustETF().searchWithQuote(Locale.ITALY, "VWCE");

// Convenience method that performs a search based on a query string including also quote info and limiting results
SearchResponse response = new JustETF().searchWithQuote(Locale.ITALY, "VWCE", 0, 10);
```
