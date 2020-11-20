package com.serosoft.academiassu.Modules.Fees;

import java.io.Serializable;

public class InvoiceDetails_Dto implements Serializable {

    private String groupingFeeHeadName;
    private String fee_head;
    private Double balanceAmount;
    private Double totalAmount;
    private Double discountAmount;
    private Double adjustedAmount;
    private Double billableAmount;
    private int currencyId;

    public InvoiceDetails_Dto(String groupingFeeHeadName, String fee_head, Double balanceAmount, Double totalAmount, Double discountAmount, Double adjustedAmount, Double billableAmount, int currencyId) {
        this.groupingFeeHeadName = groupingFeeHeadName;
        this.fee_head = fee_head;
        this.balanceAmount = balanceAmount;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.adjustedAmount = adjustedAmount;
        this.billableAmount = billableAmount;
        this.currencyId = currencyId;
    }

    public String getGroupingFeeHeadName() {
        return groupingFeeHeadName;
    }

    public String getFee_head() {
        return fee_head;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public Double getAdjustedAmount() {
        return adjustedAmount;
    }

    public Double getBillableAmount() {
        return billableAmount;
    }

    public int getCurrencyId() {
        return currencyId;
    }
}
