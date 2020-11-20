package com.serosoft.academiassu.Manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import org.json.JSONObject;

public class FeeManager extends BaseClass {

    public FeeManager(Context context) {
        super(context);
    }

    public double getReceiptAmount(JSONObject feeObject) {
        double feeAmount = 0.0;
        double pendingAmount = 0.0;
        double receiptAmount = 0.0;
        try {
            feeAmount = feeObject.optDouble("feeAmount");
            pendingAmount = feeObject.optDouble("pendingAmount");
            receiptAmount = feeAmount - pendingAmount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptAmount;
    }

    public String getReceiptNumber(JSONObject feeObject) {
        String feeHead = "";
        try {
            feeHead = feeObject.optString("receiptNo");
            if (feeHead.equals("null") || feeHead.length() == 0) {
                feeHead = feeObject.optString("raiseBillType");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeHead;
    }

    public int getCurrencyId(JSONObject feeObject) {
        int id = 0;
        try {
            id = feeObject.optInt("currencyId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getReceiptDate(JSONObject feeObject) {
        String feeDate = "";
        try {
            long fd = feeObject.optLong("paymentDate");
            if (fd != 0.0) {
                String academiaDate = BaseActivity.getAcademiaDate(fd, context);
                feeDate = academiaDate;
            } else {
                feeDate = "Not Available";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeDate;
    }

    public double getFeeAmount(JSONObject feeObject) {
        double feeAmount = 0.0;
        try {
            feeAmount = feeObject.optDouble("feeAmount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeAmount;
    }

    public double getBalanceAmount(JSONObject feeObject) {
        double feeAmount = 0.0;
        try {
            feeAmount = feeObject.optDouble("balanceAmount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeAmount;
    }

    public double getTotalAmount(JSONObject feeObject) {
        double feeAmount = 0.0;
        try {
            feeAmount = feeObject.optDouble("totalAmount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeAmount;
    }

    public String getFeesStatus(JSONObject feeObject) {
        double totalAmount = 0.0;
        double balanceAmount = 0.0;
        try {
            totalAmount = feeObject.optDouble("billableAmount");
            balanceAmount = feeObject.optDouble("balanceAmount");

            if (balanceAmount == 0.0) {
                return "Paid";
            } else if (totalAmount == balanceAmount) {
                return "Fully Pending";
            } else {
                return "Partially Settled";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getFeesStatusColor(JSONObject feeObject) {
        double totalAmount = 0.0;
        double balanceAmount = 0.0;

        try {
            totalAmount = feeObject.optDouble("billableAmount");
            balanceAmount = feeObject.optDouble("balanceAmount");

            if (balanceAmount == 0.0) {
                return context.getResources().getColor(R.color.colorForFullyPaid);
            } else if (totalAmount == balanceAmount) {
                return context.getResources().getColor(R.color.colorForNotPaid);
            } else {
                return context.getResources().getColor(R.color.colorForPartiallyPaid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getFeeHead(JSONObject feeObject) {
        String feeHead = "";
        try {
            feeHead = feeObject.optString("billNo");
            if (feeHead.equals("null") || feeHead.length() == 0) {
                feeHead = feeObject.optString("raiseBillType");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeHead;
    }

    public String getFeeDueDate(JSONObject feeObject) {
        String feeDate = "";
        try {
            long fd = feeObject.optLong("billHeaderDueDate");
            if (fd != 0.0) {
                String academiaDate = BaseActivity.getAcademiaDate(fd, context);
                feeDate = academiaDate;
            } else {
                fd = feeObject.optLong("dueDate");
                if (fd != 0.0) {
                    String academiaDate = BaseActivity.getAcademiaDate(fd, context);
                    feeDate = academiaDate;
                } else {
                    feeDate = "Not Available";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeDate;
    }

    public String getFeeDate(JSONObject feeObject) {
        String feeDate = "";
        try {
            long fd = feeObject.optLong("billHeaderBillingDate");
            if (fd != 0.0) {
                String academiaDate = BaseActivity.getAcademiaDate(fd, context);
                feeDate = academiaDate;
            } else {
                fd = feeObject.optLong("billDate");
                if (fd != 0.0) {
                    String academiaDate = BaseActivity.getAcademiaDate(fd, context);
                    feeDate = academiaDate;
                } else {
                    feeDate = "Not Available";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeDate;
    }

    /**
     * For Fee Head
     */
    public String getFeeHeadTitle(JSONObject feeObject) {
        String feeHead = "";
        try {
            feeHead = feeObject.optString("feeHeadName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeHead;
    }

    public String getDiscountAmount(JSONObject feeObject) {
        String feeHead = "";
        try {
            feeHead = feeObject.optString("discountAmount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeHead;
    }

    public double getAdjustedAmount(JSONObject feeObject) {
        double feeAmount = 0.0;
        try {
            feeAmount = feeObject.optDouble("totalAdjustedAmount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeAmount;
    }

    public double getTotalBalanceAmount(JSONObject feeObject) {
        double feeAmount = 0.0;
        try {
            feeAmount = feeObject.optDouble("totalBalanceAmount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeAmount;
    }

    public String getFeeHeadStatus(JSONObject feeObject) {

        double totalAmount = 0.0;
        double totalBalanceAmount = 0.0;
        try {
            totalAmount = feeObject.optDouble("totalAmount");
            totalBalanceAmount = feeObject.optDouble("totalBalanceAmount");

            if (totalBalanceAmount == 0.0) {
                return "Paid";
            } else if (totalAmount == totalBalanceAmount) {
                return "Fully Pending";
            } else {
                return "Partially Settled";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Drawable getFeeHeadStatusColor(JSONObject feeObject) {

        double totalAmount = 0.0;
        double totalBalanceAmount = 0.0;

        try {
            totalAmount = feeObject.optDouble("totalAmount");
            totalBalanceAmount = feeObject.optDouble("totalBalanceAmount");

            if (totalBalanceAmount == 0.0) {
                return context.getResources().getDrawable(R.drawable.background_for_paid);
            } else if (totalAmount == totalBalanceAmount) {
                return context.getResources().getDrawable(R.drawable.background_for_not_paid);
            } else {
                return context.getResources().getDrawable(R.drawable.background_for_partially_paid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getDrawable(R.drawable.background_for_paid);
    }
}
