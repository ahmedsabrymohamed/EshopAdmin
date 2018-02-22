package com.fromscratch.mine.eshopadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ProductActivity extends AppCompatActivity implements ProductAdabter.SetOncLickListener,AdapterView.OnItemSelectedListener,ProductAdabter.DeleteProduct {

    private RecyclerView productRecyclerView;
    private ProductAdabter adapter;
    private GridLayoutManager layoutManager;
    private ArrayList<Product> products;
    private ArrayList<Product> importantProduct;
    int categoryId;
    int range;
    CustomDialogClass cdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        categoryId=getIntent().getIntExtra("ID",1);
        range=getIntent().getIntExtra("Range",0);
        //set Important Product
        importantProduct=new ArrayList<>();
        products=new ArrayList<>();
        getData();
    }



    private void getData() {
        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        Call<ProductsResponse> call = apiService.getAllProducts(Integer.toString(categoryId));
        call.enqueue(new retrofit2.Callback<ProductsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductsResponse> call
                    , @NonNull retrofit2.Response<ProductsResponse> response) {
                List<Product> a=response.body().getProducts();
                products.clear();
                if(a!=null)
                {
                    for(int i=0;i<a.size();i++)
                    {
                        if(a.get(i).getRange()==range)
                        {
                            products.add(a.get(i));
                            if(a.get(i).getTrend()==range)
                                importantProduct.add(a.get(i));
                        }
                    }
                }
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
                int productNumber=((int)dpWidth/101);
                adapter =new ProductAdabter(products,ProductActivity.this,ProductActivity.this,ProductActivity.this);
                productRecyclerView=(RecyclerView) findViewById(R.id.product_list);
                layoutManager=new GridLayoutManager(ProductActivity.this,productNumber);
                productRecyclerView.setLayoutManager(layoutManager);
                productRecyclerView.setHasFixedSize(true);
                productRecyclerView.setAdapter(adapter);
                setImportantProduct();
            }
            @Override
            public void onFailure(@NonNull Call<ProductsResponse> call, @NonNull Throwable t) {
            }
        });

    }
    ImageView imageView1,imageView2,imageView3;
    TextView textView1,textView2,textView3;
    void setImportantProduct()
    {
        imageView1=(ImageView) findViewById(R.id.first_important_product_productActivity);
        imageView2=(ImageView) findViewById(R.id.second_important_product_productActivity);
        imageView3=(ImageView) findViewById(R.id.third_important_product_productActivity);
        textView1=(TextView) findViewById(R.id.first_important_product_name);
        textView2 = (TextView) findViewById(R.id.second_important_product_name);
        textView3 = (TextView) findViewById(R.id.third_important_product_name);


        if(importantProduct.size()>0)
        {
            Picasso.with(this).load(importantProduct.get(0).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView1);
            textView1.setText(importantProduct.get(0).getName());
        }
        if(importantProduct.size()>1) {
            Picasso.with(this).load(importantProduct.get(1).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView2);
            textView2.setText(importantProduct.get(1).getName());
        }
        if(importantProduct.size()>2) {
            Picasso.with(this).load(importantProduct.get(2).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView3);
            textView3.setText(importantProduct.get(2).getName());
        }
    }

    @Override
    public void SetOnclick(Product product) {
        if(product!=null) {
            Intent intent = new Intent(this, OneProductActivity.class);
            intent.putExtra("Product", product);
            startActivity(intent);
        }
        else{
            Toast.makeText(this,"No Products in this category",Toast.LENGTH_LONG).show();
        }
    }



    public void openDetails(View view) {
        Intent intent=new Intent(this,OneProductActivity.class);
        if(view.getId()==R.id.dialog_first_important_product||
                view.getId()==R.id.first_important_product_productActivity)
        {
            intent.putExtra("Product",importantProduct.get(0));
            startActivity(intent);
        }
        else if((view.getId()==R.id.dialog_second_important_product||
                view.getId()==R.id.second_important_product_productActivity)
                &&importantProduct.size()>1)
        {
            intent.putExtra("Product", importantProduct.get(1));
            startActivity(intent);
        }
        else if((view.getId()==R.id.dialog_third_important_product||
                view.getId()==R.id.third_important_product_productActivity)
                &&importantProduct.size()>2)
        {
            intent.putExtra("Product",importantProduct.get(2));
            startActivity(intent);
        }
        else
            Toast.makeText(this,"No trend in this Item",Toast.LENGTH_LONG).show();

    }

    public void change_suggest(View view) {
        if(adapter.getItemCount()>0) {
            cdd = new CustomDialogClass(this, importantProduct, products);
            cdd.show();
            cdd.setspinerClick(this);
        }
        else{
            Toast.makeText(this,"No Products in this Category",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DataLoader dataLoader=new DataLoader(this);

        if(parent.getId()==R.id.spinner1&&!parent.getItemAtPosition(position).toString().equals("Select Trend")) {
            if(importantProduct.size()>0)
            {
                dataLoader.updateTrend(Integer.toString(importantProduct.get(0).getId())
                       ,"0");

                importantProduct.set(0,products.get(position-1));
            }
            else
            {
                importantProduct.add(products.get(position-1));
            }
            Picasso.with(this).load(importantProduct.get(0).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView1);
            textView1.setText(importantProduct.get(0).getName());

            dataLoader.updateTrend(Integer.toString(importantProduct.get(0).getId())
                    ,Integer.toString(range));

            Toast.makeText(this,"first item changed",Toast.LENGTH_LONG).show();
        }
        else if(parent.getId()==R.id.spinner2&&!parent.getItemAtPosition(position).toString().equals("Select Trend")) {
            if(importantProduct.size()>1)
            {
                dataLoader.updateTrend(Integer.toString(importantProduct.get(1).getId()),"0");

                importantProduct.set(1,products.get(position-1));
            }
            else
            {
                importantProduct.add(products.get(position-1));
            }

            Picasso.with(this).load(importantProduct.get(1).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView2);
            textView2.setText(importantProduct.get(1).getName());

            dataLoader.updateTrend(Integer.toString(importantProduct.get(1).getId()),Integer.toString(range));

            Toast.makeText(this,"second item changed",Toast.LENGTH_LONG).show();
        }
        else if(parent.getId()==R.id.spinner3&&!parent.getItemAtPosition(position).toString().equals("Select Trend")) {
            if(importantProduct.size()<2)
            {
                Toast.makeText(this,"Cannot change Please Enter First And Second Trend First",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(importantProduct.size()>2)
                {
                    dataLoader.updateTrend(Integer.toString(importantProduct.get(2).getId()),"0");
                    importantProduct.set(1,products.get(position-1));
                }
                else
                {
                    importantProduct.add(products.get(position-1));
                }
                Picasso.with(this).load(importantProduct.get(2).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView3);
                textView2.setText(importantProduct.get(2).getName());

                dataLoader.updateTrend(Integer.toString(importantProduct.get(2).getId())
                        ,Integer.toString(range));

                Toast.makeText(this, "third item change", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void delet(int position) {
        String image=products.get(position).getImage().substring(products.get(position).getImage().lastIndexOf("/")+1);
        DataLoader dataLoader=new DataLoader(this);
        dataLoader.deleteProduct(image,Integer.toString(products.get(position).getId()));
        products.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editProduct(int position) {
        startActivity(new Intent(this
                ,AddNewProductActivity.class).putExtra("Product",products.get(position)));
    }

}
