package com.fromscratch.mine.eshopadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CategoryActivity extends AppCompatActivity implements CategoryAdabter.SetOncLickListener,CategoryAdabter.DeleteCategory{

    private RecyclerView categoryRecyclerView;
    private CategoryAdabter adabter;
    GridLayoutManager layoutManager;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int categoryNumber=((int)dpWidth/101);
        getData();
        adabter=new CategoryAdabter(categories,this,this,this);
        categoryRecyclerView=(RecyclerView) findViewById(R.id.category_list);
        layoutManager=new GridLayoutManager(this,categoryNumber);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(adabter);

    }

    private void getData() {
        categories=new ArrayList<>();
    }

    @Override
    public void SetOnclick(Category category) {
        Intent intent=new Intent(this,RangeActivity.class);
        intent.putExtra("ID",category.getCategory_id());
        this.startActivity(intent);
    }
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
                categories.clear();
                if(a!=null) {
                    categories.addAll(a);
                    adabter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(@NonNull Call<CategoriesResponse> call, @NonNull Throwable t) {
                // Log error here since request failed

            }
        });


        //downloadJson mdownloadJson=new downloadJson();
        //mdownloadJson.execute();



    }

    @Override
    public void Delete(int position) {
        DataLoader dataLoader=new DataLoader(this);
        dataLoader.deleteCategory(Integer.toString(categories.get(position).getCategory_id()));
        Toast.makeText(this,"you delete item" + position, Toast.LENGTH_LONG).show();
        categories.remove(position);
        adabter.notifyDataSetChanged();
    }



  /*  public void DeleteCategory(View view) {

    }*/
}
