package ilioncorp.com.jukebox.model.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class EstablishmentVO implements Serializable{
    public int id;
    public String email;
    public String phone;
    public String schedules;
    public String name;
    public String description;
    public String address;
    public String password;
    public double latitude;
    public double lenght;
    public String qrcontent;
    public String images;
    public String genders;
    public float raiting;
    public String[] imagesBar;
    public ArrayList<MenuVO> menuList;


    public String getGenders() {
        return genders;
    }

    public void setGenders(String genders) {
        this.genders = genders;
    }
    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public ArrayList<MenuVO> getMenuList() {
        return menuList;
    }

    public String getQrcontent() {
        return qrcontent;
    }

    public void setQrcontent(String qrcontent) {
        this.qrcontent = qrcontent;
    }

    public void setMenuList(ArrayList<MenuVO> menuList) {
        this.menuList = menuList;
    }

    public String[] getImagesBar() {
        return imagesBar;
    }

    public void setImagesBar(String[] imagesBar) {
        this.imagesBar = imagesBar;
    }

    public float getRaiting() {
        return raiting;
    }

    public void setRaiting(float raiting) {
        this.raiting = raiting;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchedules() {
        return schedules;
    }

    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLenght() {
        return lenght;
    }

    public void setLenght(double lenght) {
        this.lenght = lenght;
    }

}
