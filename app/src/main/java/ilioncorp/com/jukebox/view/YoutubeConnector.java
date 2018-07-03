package ilioncorp.com.jukebox.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.common.io.BaseEncoding;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import  android.content.pm.Signature;
import java.util.ArrayList;
import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.VideoItemVO;

public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;
    private Context contexto;

    // Your developer key goes here
    public static final String KEY = "AIzaSyCkdiIo0k43n61iLz_NzPOa_c9Hm2XtoAE";

    public YoutubeConnector(final Context context) {
        contexto = context;
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), hr -> {
                    String packageName = context.getPackageName();
                    String SHA1 = getSHA1(packageName);

                    hr.getHeaders().set("X-Android-Package", packageName);
                    hr.getHeaders().set("X-Android-Cert",SHA1);
                }).setApplicationName(context.getString(R.string.app_name)).build();

        try{
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setMaxResults((long)20);
                query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        }catch(IOException e){
            Log.e("ERROR YOUTUBE", "Could not initialize: "+e);
        }
    }
    private String getSHA1(String packageName){
        try {
           Signature[] signatures = contexto.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            for (Signature signature: signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-1");
                md.update(signature.toByteArray());
                return BaseEncoding.base16().encode(md.digest());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void search(String keywords, Handler handler){
        query.setQ(keywords);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<VideoItemVO> items = new ArrayList<>();
            for(SearchResult result:results){
                VideoItemVO item = new VideoItemVO();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());

                items.add(item);
            }
            Message msg = new Message();
            msg.obj = items;
            handler.sendMessage(msg);

        }catch(IOException e){
            Log.e("YOUTUBE", "Could not search: "+e);

        }
    }
}
