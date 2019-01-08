package ilioncorp.com.jukebox.view.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.BarPhotoVO;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.view.activity.FullScreenActivity;
import ilioncorp.com.jukebox.view.activity.SpacePhotoActivity;
@SuppressLint("ValidFragment")
public class PhotoListAdapter extends Fragment {

    private EstablishmentVO establishment;
    public PhotoListAdapter(EstablishmentVO establishment) {
        this.establishment =establishment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_bar,container,false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        RecyclerView recyclerView =  view.findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        BarPhotoVO photo=null;
        BarPhotoVO[] photoArray=new BarPhotoVO[establishment.getImagesBar().length];
        int i = 0;
        for(String x:establishment.getImagesBar()) {
            photo = new BarPhotoVO(x,"Photo Bar");
            photoArray[i]=photo;
            i++;
        }

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(getActivity(), photoArray);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        private BarPhotoVO[] mBarPhotoVOS;
        private Context mContext;

        public ImageGalleryAdapter(Context context, BarPhotoVO[] barPhotoVOS) {
            mContext = context;
            mBarPhotoVOS = barPhotoVOS;
        }
        @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View photoView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery,parent,false);
           ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
           return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {
            BarPhotoVO barPhotoVO = mBarPhotoVOS[position];
            ImageView imageView = holder.mPhotoImageView;
            Glide.with(mContext)
                    .load(barPhotoVO.getUrl())
                    .placeholder(R.drawable.error)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mBarPhotoVOS.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                mPhotoImageView = itemView.findViewById(R.id.iv_photoGallery);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                String[] images = new String[mBarPhotoVOS.length];
                int index=0;
                for (BarPhotoVO barPhotos:mBarPhotoVOS){
                    images[index]=barPhotos.getUrl();
                    index++;
                }
                if(position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(mContext, FullScreenActivity.class);
                    intent.putExtra("IMAGES", images);
                    mContext.startActivity(intent);
                }
            }
        }


    }

}
