package com.zhjs.scfcloud.util.enums;

/**
 * @author: dailongting
 * @date:2019/5/25 11:59
 */
public enum FileType {

    JPG(".jpg")
    ;

    String suffix;

    private FileType(String suffix){
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
