package com.example.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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

        //Customizing Gson to serialize null values
        Gson gson = new GsonBuilder().serializeNulls().create();

        //Adding Okhttp ..
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header("InterceptorHeader", "xyz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        //Setting up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create(gson)) // we pass gson to create function for customizing
                .client(okHttpClient)
                .build();
        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        getPosts();
        //getComments("posts/3/comments");
        //createPost();
        //updatePost();
        //deletePost();
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

        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");

        Call<Post> call = jsonPlaceHolderAPI.createPost(fields);

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
    private void updatePost() {
        Post post = new Post(12, null, "new text");

        Map<String, String > headers = new HashMap<>();
        headers.put("Dynamic-one", "Header one");
        headers.put("Dynamic-two", "header two");

        Call<Post> call = jsonPlaceHolderAPI.patchPost( 5, post, headers);

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
    private void deletePost() {
        Call<Void> call = jsonPlaceHolderAPI.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.setText("Code : " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }


}