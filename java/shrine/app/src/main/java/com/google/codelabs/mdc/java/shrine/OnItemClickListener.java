package com.google.codelabs.mdc.java.shrine;

import com.google.codelabs.mdc.java.shrine.java.Product;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

public interface OnItemClickListener {
    void onItemClick(ProductEntry item);
}
