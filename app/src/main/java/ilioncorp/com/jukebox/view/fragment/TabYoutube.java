package ilioncorp.com.jukebox.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.VideoItemVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.YoutubeConnector;
import ilioncorp.com.jukebox.view.adapter.VideoListAdapter;

public class TabYoutube extends Fragment implements Handler.Callback,Runnable{

    private EditText searchInput;
    private RecyclerView videosFound;
    private List<VideoItemVO> searchResults;
    private VideoListAdapter objAdaptador;
    private Handler handler;
    private String keywords;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_youtube, container, false);
        handler = new Handler(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchInput = view.findViewById(R.id.search_input);
        videosFound = view.findViewById(R.id.videos_found);
        videosFound.setHasFixedSize(true);
        videosFound.setLayoutManager(new LinearLayoutManager(getContext()));
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEARCH ){
                    searchOnYoutube(v.getText().toString());
                    return false;
                }
                return true;
            }
        });
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        searchOnYoutube("rock music");
    }
    private void searchOnYoutube(final String keywords){
        this.keywords = keywords;
        new Thread(this).start();
    }
    private void updateVideosFound(){
        if(searchResults != null) {
            objAdaptador = new VideoListAdapter(searchResults,getContext(), Constantes.idBar);
            videosFound.setAdapter(objAdaptador);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        searchResults = (List<VideoItemVO>) message.obj;
        updateVideosFound();
        return false;
    }

    @Override
    public void run() {
            YoutubeConnector yc = new YoutubeConnector(getContext());
            yc.search(keywords,handler);
            Log.e("while","even searchResult null");

    }
}
