package com.fromscratch.mine.eshopadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RangeActivity extends AppCompatActivity {
    private ArrayList<Product> importantProduct1,importantProduct2,importantProduct3;
    ArrayList<Product> products;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);
        importantProduct1=new ArrayList<>();
        importantProduct2=new ArrayList<>();
        importantProduct3=new ArrayList<>();
        products=new ArrayList<>();
        categoryId=getIntent().getIntExtra("ID",1);
        GetData();

    }

    void GetData()
    {
        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        Call<ProductsResponse> call = apiService.getAllProducts(Integer.toString(categoryId));
        call.enqueue(new retrofit2.Callback<ProductsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductsResponse> call
                    , @NonNull retrofit2.Response<ProductsResponse> response) {
                List<Product> a=response.body().getProducts();
                products.clear();
                if(a!=null)
                    products.addAll(a);
                for(int i=0;i<products.size();i++)
                {
                    if(products.get(i).getTrend()==1)
                        importantProduct1.add(products.get(i));
                    else if(products.get(i).getTrend()==2)
                        importantProduct2.add(products.get(i));
                    else if(products.get(i).getTrend()==3)
                        importantProduct3.add(products.get(i));
                }
                setImportantProduct();

            }
            @Override
            public void onFailure(@NonNull Call<ProductsResponse> call, @NonNull Throwable t) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setImportantProduct();

    }

    void setImportantProduct(){
        /////////////////////////////////////////////////////

        if(importantProduct1.size()>0)
        {
            ImageView imageView1=(ImageView) findViewById(R.id.low_range_first_important_product);
            TextView textView1=(TextView) findViewById(R.id.low_range_first_important_product_name);
            Picasso.with(this).load(importantProduct1.get(0).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView1);
            textView1.setText(importantProduct1.get(0).getName());
        }
        if(importantProduct1.size()>1) {
            ImageView imageView2=(ImageView) findViewById(R.id.low_range_second_important_product);
            TextView textView2 = (TextView) findViewById(R.id.low_range_second_important_product_name);
            Picasso.with(this).load(importantProduct1.get(1).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView2);
            textView2.setText(importantProduct1.get(1).getName());

        }
        if(importantProduct1.size()>2) {
            ImageView imageView3=(ImageView) findViewById(R.id.low_range_third_important_product);
            Picasso.with(this).load(importantProduct1.get(2).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView3);
            TextView textView3 = (TextView) findViewById(R.id.low_range_third_important_product_name);
            textView3.setText(importantProduct1.get(2).getName());

        }

        /////////////////////////////////////////////////////

        if(importantProduct2.size()>0)
        {
            ImageView imageView1=(ImageView) findViewById(R.id.mid_range_first_important_product);
            TextView textView1=(TextView) findViewById(R.id.mid_range_first_important_product_name);
            Picasso.with(this).load(importantProduct2.get(0).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView1);
            textView1.setText(importantProduct2.get(0).getName());
        }
        if(importantProduct2.size()>1) {
            ImageView imageView2=(ImageView) findViewById(R.id.mid_range_second_important_product);
            TextView textView2 = (TextView) findViewById(R.id.mid_range_second_important_product_name);
            Picasso.with(this).load(importantProduct2.get(1).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView2);
            textView2.setText(importantProduct2.get(1).getName());

        }
        if(importantProduct2.size()>2) {
            ImageView imageView3=(ImageView) findViewById(R.id.mid_range_third_important_product);
            Picasso.with(this).load(importantProduct2.get(2).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView3);
            TextView textView3 = (TextView) findViewById(R.id.mid_range_third_important_product_name);
            textView3.setText(importantProduct2.get(2).getName());
        }

        /////////////////////////////////////////////////////

        if(importantProduct3.size()>0)
        {
            ImageView imageView1=(ImageView) findViewById(R.id.high_range_first_important_product);
            TextView textView1=(TextView) findViewById(R.id.high_range_first_important_product_name);
            Picasso.with(this).load(importantProduct3.get(0).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView1);
            textView1.setText(importantProduct3.get(0).getName());
        }
        if(importantProduct3.size()>1) {
            ImageView imageView2=(ImageView) findViewById(R.id.high_range_second_important_product);
            TextView textView2 = (TextView) findViewById(R.id.high_range_second_important_product_name);
            Picasso.with(this).load(importantProduct3.get(1).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView2);
            textView2.setText(importantProduct3.get(1).getName());

        }
        if(importantProduct3.size()>2) {
            ImageView imageView3=(ImageView) findViewById(R.id.high_range_third_important_product);
            Picasso.with(this).load(importantProduct3.get(2).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView3);
            TextView textView3 = (TextView) findViewById(R.id.high_range_third_important_product_name);
            textView3.setText(importantProduct3.get(2).getName());

        }
    }
    public void openDetils(View view) {}

    public void openDetils1(View view) {
        Intent intent=new Intent(this,ProductActivity.class);
        intent.putExtra("ID",categoryId);
        intent.putExtra("Range",1);
        this.startActivity(intent);
    }

    public void openDetils2(View view) {
        Intent intent=new Intent(this,ProductActivity.class);
        intent.putExtra("ID",categoryId);
        intent.putExtra("Range",2);
        this.startActivity(intent);
    }
    public void openDetils3(View view) {
        Intent intent=new Intent(this,ProductActivity.class);
        intent.putExtra("ID",categoryId);
        intent.putExtra("Range",3);
        this.startActivity(intent);
    }

}
