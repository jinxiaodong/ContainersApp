
package com.project.container.network.parser;


import com.project.container.network.assist.HttpHandler;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.request.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import okio.BufferedSource;

public class DownloadResponseParser<T> extends ResponseParser<T> {

    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/
    private String downloadFilePath;
    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/
    @Override
    public ResponseData parser(Response response) {
        ResponseData data = new ResponseData();
        try {
            data.status = true;
            if(response == null){
                return data;
            }
            BufferedSource source = response.body().source();
            long totalCount = response.body().contentLength();
            byte[] buf = new byte[2048];
            int len = 0;
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = source.inputStream();

                long hasRead = 0;
                if(totalCount == -1) {
                    totalCount = is.available();
                }

                if(totalCount == 0) {
                    data.message = "文件内容为空";
                    data.code = "200";
                    return data;
                }

                HttpHandler.get().onProcess(totalCount,hasRead,request.getUserCallBack());

                makeParentDir(downloadFilePath);
                File file = new File(downloadFilePath);
                if(file.isDirectory()) {
                    data.status = false;
                    data.message = "保存路径错误";
                    return data;
                }
                if(file.exists()) {
                    file.delete();
                }
                fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    hasRead += len;
                    HttpHandler.get().onProcess(totalCount,hasRead,request.getUserCallBack());
                }
                fos.flush();
                data.message = "下载成功";
                data.code = "200";
                return data;
            } catch (Exception e) {
                data.status = false;
                data.message = "下载失败";
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                }
            }

        }catch (Exception e){
            data.status = false;
            data.message = "下载错误";
        }
        return data;
    }
    /*******************************************************************************
     *	Public/Protected Methods
     *******************************************************************************/
    public DownloadResponseParser(HttpRequest<T> request, String filePath){
        super(request);
        this.downloadFilePath = filePath;
    }

    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/
    /**
     * Author: hyl
     * Time: 2015-8-21下午11:13:35
     * @param path
     */
    private void makeParentDir(String path) {
        String parentPath = getParentPath(path);
        File file = new File(parentPath);
        if(!file.exists()) {
            makeParentDir(parentPath);
            file.mkdir();
        }
    }

    /**
     * Author: hyl
     * Time: 2015-8-21下午11:12:44
     * @param path
     * @return
     */
    private String getParentPath(String path) {
        if (path.equals("/")) {
            return path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        path = path.substring(0, path.lastIndexOf("/"));
        return path.equals("") ? "/" : path;
    }
    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
