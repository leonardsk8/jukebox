package ilioncorp.com.jukebox.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class BarPhotoVO implements Parcelable {
    private String mUrl;
    private String mTitle;
    private BarPhotoVO[] photos;

    public BarPhotoVO[] getPhotos() {
        return photos;
    }

    public void setPhotos(BarPhotoVO[] photos) {
        this.photos = photos;
    }

    public BarPhotoVO(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected BarPhotoVO(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<BarPhotoVO> CREATOR = new Creator<BarPhotoVO>() {
        @Override
        public BarPhotoVO createFromParcel(Parcel in) {
            return new BarPhotoVO(in);
        }

        @Override
        public BarPhotoVO[] newArray(int size) {
            return new BarPhotoVO[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static  BarPhotoVO[] getSpacePhotos() {

        return new BarPhotoVO[]{
                new BarPhotoVO("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                new BarPhotoVO("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                new BarPhotoVO("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
                new BarPhotoVO("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                new BarPhotoVO("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                new BarPhotoVO("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
        };
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}
