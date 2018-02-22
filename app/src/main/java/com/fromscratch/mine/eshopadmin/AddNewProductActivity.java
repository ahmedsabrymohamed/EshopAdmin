package com.fromscratch.mine.eshopadmin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;



import java.io.IOException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AddNewProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<Category> categories;
    ArrayList<String> categoriesItem;
    EditText name, price,  description;
    private int PICK_IMAGE_REQUEST = 1;
    String range;
    Uri uri;
    boolean flag;
    Product product;
    int userChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        if(getIntent()!=null){
            product=getIntent().getParcelableExtra("Product");
            flag=product!=null;
        }

        if(savedInstanceState!=null){
            product=savedInstanceState.getParcelable("Product");
            flag=savedInstanceState.getBoolean("flag",false);
            userChoice=savedInstanceState.getInt("userChoiceSpinner",-1);
        }
        name = (EditText) findViewById(R.id.new_product_name);
       // range = (EditText) findViewById(R.id.new_product_range);
        price = (EditText) findViewById(R.id.new_product_price);
        description = (EditText) findViewById(R.id.new_product_description);
        isStoragePermissionGranted();
        range="1";
        if(flag){
            name.setText(product.getName());
            price.setText(product.getPrice());
            description.setText(product.getDescription());
            range=Integer.toString(product.getRange());
        }
        setRadioButton(range);


    }

    Spinner spinner;

    @Override
    protected void onStart() {
        super.onStart();
        ApiInterface apiService =
                APIClient.getClient().create(ApiInterface.class);

        Call<CategoriesResponse> call = apiService.getAllCategories();
        call.enqueue(new retrofit2.Callback<CategoriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoriesResponse> call
                    , @NonNull retrofit2.Response<CategoriesResponse> response) {

                List<Category> a = response.body().getCategories();
                categories = new ArrayList<>();
                categories.clear();
                if (a != null)
                    categories.addAll(a);

                categoriesItem = new ArrayList<>();
                for (int i = 0; i < categories.size(); i++) {
                    categoriesItem.add(categories.get(i).getName());
                    if(flag&&categories.get(i).getCategory_id()==product.getCategory_id())
                        userChoice=i;
                }
                spinner = (Spinner) findViewById(R.id.spinner);
                spinner.setOnItemSelectedListener(AddNewProductActivity.this);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddNewProductActivity.this, R.layout.spinner_layout, categoriesItem);
                dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                spinner.setAdapter(dataAdapter);

                if (userChoice != -1) {
                    // set the selected value of the spinner
                    spinner.setSelection(userChoice);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoriesResponse> call, @NonNull Throwable t) {
                // Log error here since request failed

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            uri = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.product_new_image);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        userChoice = spinner.getSelectedItemPosition();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void AddNewProduct(View view) {
        DataLoader dl = new DataLoader(this);
        if (!name.getText().toString().isEmpty()
             //   && !range.getText().toString().isEmpty()
                && !price.getText().toString().isEmpty()
                && !description.getText().toString().isEmpty()
                && spinner.getSelectedItem() != null
                ) {
            if(uri==null&&!flag)
            {

                Toast.makeText(this, "You must Add Image", Toast.LENGTH_LONG).show();
            }
            else if(flag){
                dl.updateProduct(Integer.toString(product.getId())
                        ,Integer.toString(categories.get(spinner.getSelectedItemPosition()).getCategory_id())
                        , name.getText().toString()
                        , description.getText().toString()
                        , price.getText().toString()
                        , Double.toString(product.getRate())
                        , product.getImage(), uri,Integer.toString(product.getTrend()) ,range);
                Toast.makeText(this, "Product Updated Successfully", Toast.LENGTH_LONG).show();
            }
            else if(!flag) {
                dl.createProduct(Integer.toString(categories.get(spinner.getSelectedItemPosition()).getCategory_id()), name.getText().toString(), description.getText().toString(), price.getText().toString(), "0", "UnKnown", uri, range);
                Toast.makeText(this, "You add new Product", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Fill all the Fields", Toast.LENGTH_LONG).show();
        }
    }

    public void bickImage(View view) {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture")
                , PICK_IMAGE_REQUEST);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            //Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            Toast.makeText(this, "You Must Allow PERMISSION to Uplaod Images ", Toast.LENGTH_LONG)
                    .show();
            finish();

        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.radio_medium:
                if (checked)
                    range="2";
                    break;
            case R.id.radio_high:
                if (checked)
                    range="3";
                    break;
            default:
                range="1";

        }
    }
    public void setRadioButton(String range) {
        // Is the button now checked?
        RadioGroup checked = ((RadioGroup) findViewById(R.id.RadioG));

        // Check which radio button was clicked
        switch(range) {

            case "1":
               checked.check(R.id.radio_low);
                break;
            case "2":
                checked.check(R.id.radio_medium);
                break;
            default:
                checked.check(R.id.radio_high);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("userChoiceSpinner",userChoice);
        outState.putBoolean("flag",flag);
        outState.putParcelable("Product",product);
    }

}
