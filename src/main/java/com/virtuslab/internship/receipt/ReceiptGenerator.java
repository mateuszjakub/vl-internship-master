package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.Discounts;
import com.virtuslab.internship.product.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.frequency;

@Service
public class ReceiptGenerator {

    public Receipt generate(Basket basket) {

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        Set<Product> products = new HashSet<>();

        for (Product product : basket.getProducts()) {
            if (!products.contains(product)) {
                ReceiptEntry receiptEntry = new ReceiptEntry(product, (frequency(basket.getProducts(), product)));
                receiptEntries.add(receiptEntry);
                products.add(product);
            }
        }

        Receipt receipt = new Receipt(receiptEntries);
        for (Discounts discount : Discounts.values()) {
            receipt = discount.apply(receipt);
        }
        return receipt;
    }

}
