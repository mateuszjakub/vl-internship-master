package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public enum Discounts {

    FIFTEEN_PERCENT_DISCOUNT(15) {
        @Override
        public boolean shouldApply(Receipt receipt) {
            int counter = 0;
            for (ReceiptEntry recipeEntry : receipt.entries()) {

                if (recipeEntry.product().type().equals(PRODUCT_TYPE)) {
                    counter += recipeEntry.quantity();
                }
            }
            return counter >= 3;
        }
    },
    TEN_PERCENT_DISCOUNT(10) {
        @Override
        public boolean shouldApply(Receipt receipt) {
            return receipt.totalPrice().compareTo(BigDecimal.valueOf(DISCOUNT_THRESHOLD)) >= 0;
        }
    };

    public BigDecimal value;
    private static final int DISCOUNT_THRESHOLD = 50;
    public static final Product.Type PRODUCT_TYPE = Product.Type.GRAINS;

    Discounts(int value) {
        this.value = BigDecimal.valueOf(value);
    }

    public abstract boolean shouldApply(Receipt receipt);

    public BigDecimal getPercentageOfTotalPrice() {
        return BigDecimal.valueOf(100).subtract(value).divide(BigDecimal.valueOf(100));
    }

    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(getPercentageOfTotalPrice());
            List<String> discounts = new ArrayList<>();
            discounts.add(this.name());

            return new Receipt(receipt.entries(), discounts, totalPrice);
        }
        return receipt;
    }
}
