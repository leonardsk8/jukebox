package ilioncorp.com.jukebox.view.activity;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.graphics.Palette;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.BarPhotoVO;
import ilioncorp.com.jukebox.view.generic.GenericActivity;



public class SpacePhotoActivity extends GenericActivity {
    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bar_photo);

        mImageView = findViewById(R.id.ivImageSpacePhoto);
        BarPhotoVO barPhotoVO = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);

        Glide.with(this)
                .load(barPhotoVO.getUrl())
                .asBitmap()
                .error(R.drawable.error)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        onPalette(Palette.from(resource).generate());
                        mImageView.setImageBitmap(resource);
                        return false;
                    }
                    public void onPalette(Palette palette) {
                        if (null != palette) {
                            ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
                        }
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);
    }
}
