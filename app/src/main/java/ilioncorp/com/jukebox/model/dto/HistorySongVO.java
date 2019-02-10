package ilioncorp.com.jukebox.model.dto;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistorySongVO implements Comparable<HistorySongVO> {


    private String idUser;
    private String nameSong;
    private String dateSong;
    private String stateSong;
    private String thumnailSong;
    private String nameBar;
    private String videoIdSong;
    private String idBar;

    public String getIdBar() {
        return idBar;
    }

    public void setIdBar(String idBar) {
        this.idBar = idBar;
    }

    public String getVideoIdSong() {
        return videoIdSong;
    }

    public void setVideoIdSong(String videoIdSong) {
        this.videoIdSong = videoIdSong;
    }

    public String getNameBar() {
        return nameBar;
    }

    public void setNameBar(String nameBar) {
        this.nameBar = nameBar;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDateSong() {
        return dateSong;
    }

    public void setDateSong(String dateSong) {
        this.dateSong = dateSong;
    }

    public String getStateSong() {
        return stateSong;
    }

    public void setStateSong(String stateSong) {
        this.stateSong = stateSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getThumnailSong() {
        return thumnailSong;
    }

    public void setThumnailSong(String thumnailSong) {
        this.thumnailSong = thumnailSong;
    }

    @Override
    public int compareTo(@NonNull HistorySongVO historySongVO) {
        if (getDateSong() == null || historySongVO.getDateSong() == null)
            return 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date fecha1 = null ;
        Date fecha2 = null ;
        try {
            fecha1=dateFormat.parse(getDateSong());
            fecha2=dateFormat.parse(historySongVO.getDateSong());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha2.compareTo(fecha1);
    }
}
