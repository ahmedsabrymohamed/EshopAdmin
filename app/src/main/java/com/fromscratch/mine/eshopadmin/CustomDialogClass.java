package com.fromscratch.mine.eshopadmin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CustomDialogClass extends Dialog implements  View.OnClickListener {
    public Context c;

    ArrayList<Product> importantProduct,products;
    ImageView firstImage,secondImage,thirdImage;
    TextView firstName,secondName,thirdName;
    Spinner spinner1,spinner2,spinner3;
    public void setspinerClick(AdapterView.OnItemSelectedListener c)
    {
        spinner1.setOnItemSelectedListener(c);
        spinner2.setOnItemSelectedListener(c);
        spinner2.setSelected(false);
        spinner3.setOnItemSelectedListener(c);
        spinner3.setSelected(false);
    }

    public CustomDialogClass(Context context, ArrayList<Product> importantProduct,ArrayList<Product>products) {
        super(context);
        this.importantProduct=importantProduct;
        c=context;
        this.products=products;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        firstImage = (ImageView) findViewById(R.id.dialog_first_important_product);
        secondImage = (ImageView) findViewById(R.id.dialog_second_important_product);
        thirdImage = (ImageView) findViewById(R.id.dialog_third_important_product);
        firstName=(TextView) findViewById(R.id.dialog_first_important_product_name);
        secondName=(TextView) findViewById(R.id.dialog_second_important_product_name);
        thirdName=(TextView) findViewById(R.id.dialog_third_important_product_name);
          // Spinner element
         spinner1 = (Spinner) findViewById(R.id.spinner1);
         spinner2= (Spinner) findViewById(R.id.spinner2);
         spinner3 = (Spinner) findViewById(R.id.spinner3);

        /*// Spinner click listener
        spinner.setOnItemSelectedListener(c);*/

        List<String> items = new ArrayList<>();
        items.add("Select Trend");
        for(int i=0;i<products.size();i++)
        {
            items.add(products.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(c, R.layout.spinner_layout,items );
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        if(importantProduct.size()>0)
        {
            Picasso.with(c).load(importantProduct.get(0).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(firstImage);
            firstName.setText(importantProduct.get(0).getName());
        }
        if(importantProduct.size()>1) {
            Picasso.with(c).load(importantProduct.get(1).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(secondImage);
            secondName.setText(importantProduct.get(1).getName());
        }
        if(importantProduct.size()>2) {
            Picasso.with(c).load(importantProduct.get(2).getImage()).resize(200,200).placeholder(R.drawable.no_image).into(thirdImage);
            thirdName.setText(importantProduct.get(2).getName());
        }
        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);
        spinner3.setAdapter(dataAdapter);
        spinner1.setSelected(false);
        spinner2.setSelected(false);
        spinner3.setSelected(false);


    }

    @Override
    public void onClick(View view) {

    }


}