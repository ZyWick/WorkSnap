package com.mobdeve.s15.worksnap;

public class MyPhotoData {
    public Integer getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(Integer photoImage) {
        this.photoImage = photoImage;
    }

    private Integer photoImage;

    public MyPhotoData(Integer photoImage)     {
        this.photoImage = photoImage;
    }
}
