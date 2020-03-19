package com.forkliu.demo.retrofitfileupload;

import java.util.List;

public class UploadImageApiResponse {
    boolean error;
    String message;
    List<ImageFile> images;

    public boolean getError(){
        return error;
    }

    public String getMessage(){
        return message;
    }

    List<ImageFile> getImages(){
        return images;
    }

}
