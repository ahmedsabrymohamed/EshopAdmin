package com.fromscratch.mine.eshopadmin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AllComment extends AppCompatActivity implements CommentAdabter.SetOncLickListener ,CommentAdabter.RejectComment,CommentAdabter.AcceptComment{
    private ArrayList<Comment> comments;
    private CommentAdabter adabter;
    private LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;

    RecyclerView commentRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);
        getcomment();
    }

    public  void getcomment()
    {
        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        Call<CommentsResponse> call = apiService.getAllCommentsAdmin();
        call.enqueue(new retrofit2.Callback<CommentsResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommentsResponse> call
                    , @NonNull retrofit2.Response<CommentsResponse> response) {

                if(response.body()!=null) {
                    List<Comment> a = response.body().getComments();
                    if (a == null) {
                        Toast.makeText(AllComment.this,"No Comment to Review",Toast.LENGTH_LONG).show();
                    } else {
                        comments = new ArrayList<>();
                        for (int i = 0; i < a.size(); i++) {
                            if (a.get(i).getAppear() == 0)
                                comments.add(a.get(i));
                        }
                        commentRecyclerView = (RecyclerView) findViewById(R.id.AllComment);
                        layoutManager = new LinearLayoutManager(AllComment.this);
                        adabter = new CommentAdabter(comments, AllComment.this, AllComment.this, AllComment.this, AllComment.this, 1);
                        commentRecyclerView.setLayoutManager(layoutManager);
                        commentRecyclerView.setAdapter(adabter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentsResponse> call, @NonNull Throwable t) {
                // Log error here since request failed

            }
        });


    }


    public void AcceptComment(View view) {
    }

    @Override
    public void Reject(int position) {
        DataLoader dataLoader=new DataLoader(this);
        dataLoader.deleteComment(Integer.toString(comments.get(position).getCid()));
        //Toast.makeText(this,"you want to delete item" + position+1, Toast.LENGTH_LONG).show();
        comments.remove(position);
        adabter.notifyDataSetChanged();
    }

    @Override
    public void Accept(int position) {
        DataLoader dataLoader=new DataLoader(this);
        dataLoader.updateComment(Integer.toString(comments.get(position).getCid()),comments.get(position).getBody(),comments.get(position).getCname(),comments.get(position).getEmail(),"1");
        comments.remove(position);
        adabter.notifyDataSetChanged();
    }

    @Override
    public void SetOnclick(Comment comment) {

    }
}
