package it.pyrox.justetf.model;

public class Quote {

    private LatestQuote latestQuote;
    private String latestQuoteDate;
    private PreviousQuote previousQuote;
    private String previousQuoteDate;
    private DtdPrc dtdPrc;
    private DtdAmt dtdAmt;
    private String quoteTradingVenue;
    private QuoteLowHigh quoteLowHigh;

    public LatestQuote getLatestQuote() {
        return latestQuote;
    }

    public void setLatestQuote(LatestQuote latestQuote) {
        this.latestQuote = latestQuote;
    }

    public String getLatestQuoteDate() {
        return latestQuoteDate;
    }

    public void setLatestQuoteDate(String latestQuoteDate) {
        this.latestQuoteDate = latestQuoteDate;
    }

    public PreviousQuote getPreviousQuote() {
        return previousQuote;
    }

    public void setPreviousQuote(PreviousQuote previousQuote) {
        this.previousQuote = previousQuote;
    }

    public String getPreviousQuoteDate() {
        return previousQuoteDate;
    }

    public void setPreviousQuoteDate(String previousQuoteDate) {
        this.previousQuoteDate = previousQuoteDate;
    }

    public DtdPrc getDtdPrc() {
        return dtdPrc;
    }

    public void setDtdPrc(DtdPrc dtdPrc) {
        this.dtdPrc = dtdPrc;
    }

    public DtdAmt getDtdAmt() {
        return dtdAmt;
    }

    public void setDtdAmt(DtdAmt dtdAmt) {
        this.dtdAmt = dtdAmt;
    }

    public String getQuoteTradingVenue() {
        return quoteTradingVenue;
    }

    public void setQuoteTradingVenue(String quoteTradingVenue) {
        this.quoteTradingVenue = quoteTradingVenue;
    }

    public QuoteLowHigh getQuoteLowHigh() {
        return quoteLowHigh;
    }

    public void setQuoteLowHigh(QuoteLowHigh quoteLowHigh) {
        this.quoteLowHigh = quoteLowHigh;
    }

}
