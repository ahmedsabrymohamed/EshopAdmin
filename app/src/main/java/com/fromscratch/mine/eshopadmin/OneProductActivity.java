package com.fromscratch.mine.eshopadmin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class OneProductActivity extends AppCompatActivity implements CommentAdabter.SetOncLickListener ,CommentAdabter.RejectComment,CommentAdabter.AcceptComment{

    private ArrayList<Comment> comments;
    private CommentAdabter adabter;
    private LinearLayoutManager layoutManager;
    RecyclerView commentRecyclerView;
    private Product product;
    ImageView imageView;
    RatingBar ratingBar;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one_product);
        product=getIntent().getParcelableExtra("Product");
        imageView=(ImageView) findViewById(R.id.product_details_image) ;
        Picasso.with(this).load(product.getImage()).resize(200,200).placeholder(R.drawable.no_image).into(imageView);

        TextView textView=(TextView) findViewById(R.id.product_details_name);
        textView.setText(product.getName());
        TextView textView1=(TextView) findViewById(R.id.Description);
        textView1.setText(product.getDescription());
        ratingBar=(RatingBar) findViewById(R.id.product_rate);

        getCommentData();
        commentRecyclerView=(RecyclerView) findViewById(R.id.comment_list);
        layoutManager=new LinearLayoutManager(this);
        adabter=new CommentAdabter(comments,this,this,this,this,0);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(adabter);
    }

    private void getCommentData() {
        comments=new ArrayList<>();



    }

    @Override
    public void SetOnclick(Comment comment) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        Call<CommentsResponse> call = apiService.getAllComments(Integer.toString(product.getId()));
        call.enqueue(new retrofit2.Callback<CommentsResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommentsResponse> call
                    , @NonNull retrofit2.Response<CommentsResponse> response) {
                List<Comment> a=response.body().getComments();
                comments.clear();
                if(a!=null) {
                    for(int i=0;i<a.size();i++)
                    {
                        if(a.get(i).getAppear()==1)
                            comments.add(a.get(i));
                    }
                }
                adabter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<CommentsResponse> call, @NonNull Throwable t) {
                // Log error here since request failed

            }
        });


    }

    @Override
    public void Reject(int comment) {

    }

    @Override
    public void Accept(int comment) {

    }

    public void AddComment(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            EditText editText=(EditText) findViewById(R.id.commentText);
            String s=editText.getText().toString();
            DataLoader dataLoader=new DataLoader(this);
            dataLoader.createComment(Integer.toString(product.getId()),s,name,email);
            Toast.makeText(this,"Your comment Under Review",Toast.LENGTH_LONG).show();

        }
    }

    public void changeRate(View view) {
        Toast.makeText(this,"Thank You For The Rate",Toast.LENGTH_LONG).show();
        float newRate=ratingBar.getRating();
        int newRate1= (int) ((product.getRate()+newRate)/2);
        if(newRate1>5)
            newRate1=5;
        ratingBar.setRating(newRate1);
        DataLoader dataLoader=new DataLoader(this);
        dataLoader.updateProduct(Integer.toString(product.getId()),Integer.toString(product.getCategory_id()),product.getName(),product.getDescription(),product.getPrice(),Integer.toString(newRate1),product.getImage(),null,Integer.toString(product.getTrend()),Integer.toString(product.getRange()));
    }
}
