package com.example.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private JsonPlaceHolderAPI jsonPlaceHolderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.results);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        //getPosts();
        //getComments("posts/3/comments");
        createPost();
    }

    private void getPosts() {

        //This is a Map for the second approach to pass queries :)

        Map<String, String> params = new HashMap<>();
        params.put("userId", "2");
        params.put("_sort", "id");
        params.put("_order", "desc");

        Call<List<Post>> call = jsonPlaceHolderAPI.getPosts(params);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    result.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();
                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "userID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n\n";

                    result.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    private void getComments(String url) {
        Call<List<Comment>> call = jsonPlaceHolderAPI.getComments(url);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()){
                    result.setText("Code : "+ response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for (Comment comment : comments) {
                    String content = "";
                    content += "ID : " + comment.getId() + "\n";
                    content += "Post ID : " + comment.getPostId() + "\n";
                    content += "Name : " + comment.getName() + "\n";
                    content += "Email : " + comment.getEmail() + "\n";
                    content += "Text : " + comment.getText() + "\n\n";

                    result.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    private void createPost() {
        Post post = new Post(23, "New Title", "New text");
        Call<Post> call = jsonPlaceHolderAPI.createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    result.setText("Code : " + response.code());
                    return;
                }
                Post postResponse = response.body();

                String content = "";
                content += "Code Response : "+ response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "userID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n\n";

                result.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }
}