package ilioncorp.com.jukebox.model.dto;


public class ReportVO {

    private String userId;
    private String hora;
    private String songId;
    private String fecha;
    private String day;



    public ReportVO() {
    }

    public ReportVO(String userId, String hora) {
        this.userId = userId;
        this.hora = hora;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
