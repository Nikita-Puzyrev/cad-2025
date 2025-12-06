package com.musicstore.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musicstore.android.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    
    private List<Product> productList;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }
    
    public ProductAdapter(List<Product> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }
    
    public void updateData(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, listener);
    }
    
    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }
    
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productStock, productCategory;
        ImageView editButton, deleteButton;
        
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productStock = itemView.findViewById(R.id.product_stock);
            productCategory = itemView.findViewById(R.id.product_category);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
        
        public void bind(Product product, OnItemClickListener listener) {
            productName.setText(product.getName());
            productPrice.setText(product.getFormattedPrice());
            productStock.setText(product.getFormattedStock());
            productCategory.setText(product.getCategoryName());
            productCategory.setTextColor(itemView.getContext()
                    .getResources().getColor(product.getCategoryColor()));
            
            // Обработка кликов
            itemView.setOnClickListener(v -> listener.onItemClick(product));
            editButton.setOnClickListener(v -> listener.onEditClick(product));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(product));
        }
    }
}