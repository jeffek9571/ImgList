package com.example.imglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {
    ImageView iv;
    CircleImageView circleImageView;
    Spinner page,limit;
    File file,appfile,file1,file2;
    RecyclerView rv;
    Adapter ad;
    ArrayList<Post> mpost;
    String total,url,result;
    Post[] posts;



    static final String[] mPagelist=new String[] {"1", "2", "3", "4", "5"};
    static final String[] mLimitlist=new String[] {"5", "10", "15"};
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public int pageNum,limitNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        iv=findViewById(R.id.image);
        circleImageView=findViewById(R.id.image);
        rv=findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mpost=new ArrayList<>();


//        circleImageView.setImageResource(R.drawable.f003);

        page=findViewById(R.id.sp1);
        limit=findViewById(R.id.sp2);



        checkPermission();
        init();
        readData();

        ArrayAdapter<String> mPageadapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,mPagelist);
        ArrayAdapter<String> mLimitadapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,mLimitlist);


        page.setAdapter(mPageadapter);
        if(pageNum>=0){
            page.setSelection(pageNum,true);
        }

        limit.setAdapter(mLimitadapter);
        if(limitNum>=0){
            limit.setSelection(limitNum,true);
        }


        page.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pageNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                page.setSelection(pageNum,true);
            }
        });

        limit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                limitNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                limit.setSelection(limitNum,true);
            }
        });

    }

    private void readData(){
        if(new File(appfile,"Page.txt").exists() && new File(appfile,"Limit.txt").exists()){
//            Log.d("Pagetxt", "ok");
            try{
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
                BufferedReader bufferedReader1 =new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
                String page,limit;

                StringBuffer stringBuffer =new StringBuffer();
                StringBuffer stringBuffer1 =new StringBuffer();
                if((page = bufferedReader.readLine())!=null && (limit = bufferedReader1.readLine())!=null){
                    stringBuffer.append(page);
                    stringBuffer1.append(limit);
                }

                bufferedReader.close();
                bufferedReader1.close();

                pageNum=Integer.parseInt(stringBuffer.toString());
                limitNum=Integer.parseInt(stringBuffer1.toString());

//                Log.d("pagenum", "onCreate: "+pageNum+","+limitNum);


            }catch (Exception e){
                Log.d("errorx", e.toString());
            }

        }
    }

    private void saveData(){
        try {
            FileOutputStream fileOutputStream =new FileOutputStream(file1);
            fileOutputStream.write(String.valueOf(pageNum).getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();

            FileOutputStream fileOutputStream1 =new FileOutputStream(file2);
            fileOutputStream1.write(String.valueOf(limitNum).getBytes());
            fileOutputStream1.flush();
            fileOutputStream1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(){

        file =Environment.getExternalStorageDirectory();
//        Log.v("path", file.getAbsolutePath());
//        sdroot=new File(file,"XX.txt");
        appfile = new File(file,"Android/data/"+ getPackageName()+"/");
        if(!appfile.exists()){
            if(appfile.mkdir()){
                Log.v("mkdir","mkdir OK");
            }
            else{
                Log.v("mkdir","mkdir Fail");
            }
        }
        file1=new File(appfile,"Page.txt");
        file2=new File(appfile,"Limit.txt");
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }
        else{

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            init();
        }
        else{
            finish();
        }

        }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        saveData();
    }

    public void Button(View view)  {
        mpost.clear();
        okhttp();
    }

    public void okhttp(){

        if(pageNum>=0 && limitNum>=0){
            String url1 = "https://picsum.photos/v2/list?page="+mPagelist[pageNum]+"&limit="+mLimitlist[limitNum];
            Log.d("Post", "url: "+url1);

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url(url1)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    result = response.body().string();
                    posts = new Gson().fromJson(result, Post[].class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (Post item : posts) {
                                    Log.d("Post", "id: " + item.getId());
                                    Log.d("Post", "Height: " + item.getHeight());
                                    Log.d("Post", "Width: " + item.getWidth());
                                    Log.d("Post", "Author: " + item.getAuthor());
                                    Log.d("Post", "Download_url: " + item.getDownload_url());
                                    total = String.valueOf(item.getAuthor()) + "   " + String.valueOf(item.getWidth()) + "x" + String.valueOf(item.getHeight());
                                    url = String.valueOf(item.getDownload_url());

                                    Log.d("total", total + "\n" + url);
                                    mpost.add(new Post(total, url));
                                }
                                ad = new Adapter(MainActivity.this, mpost);
                                rv.setAdapter(ad);
                            }catch (Exception e){
                                Log.d("e", e.toString());
                            }
                        }
                    });


                }

            });
        }
    }

}
