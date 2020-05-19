package com.example.imglist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.AdViewHolder> {

    Context mContext;
    ArrayList<Post> mPost;




    public Adapter(Context context,ArrayList<Post> post){
        mContext=context;
        mPost=post;
        Log.d("find", "2");
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("find", "3");
        View view =LayoutInflater.from(mContext).inflate(R.layout.listitem,parent,false);
        AdViewHolder av=new AdViewHolder(view);
        return av;

    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Log.d("find", "6");
        Post post=mPost.get(position);
        final String download_url=post.getDownload_url();
        String total=post.getTotal();
//        int width=post.getWidth();
//        int height=post.getHeight();

        holder.tv3.setText(total);
        Log.d("bind1", total+"\n"+download_url);
        Picasso.get().load(download_url).fit().centerInside().into(holder.image);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ie = new Intent(Intent.ACTION_VIEW, Uri.parse(download_url));
                mContext.startActivity(ie);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mPost.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView image;
//        public ImageView image;
        public TextView tv3;
        View v;


        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("find", "5");
            v=itemView;
            image=itemView.findViewById(R.id.image);
            tv3=itemView.findViewById(R.id.tv3);
        }
    }

}
