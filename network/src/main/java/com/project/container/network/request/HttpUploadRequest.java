package com.project.container.network.request;

import android.content.Context;

import com.project.container.network.assist.HttpFile;
import com.project.container.network.http.HttpClient;
import com.project.container.network.parser.ResponseParser;
import com.project.container.network.parser.UploadResponseParser;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 上传文件request
 */
public class HttpUploadRequest<T> extends HttpRequest<T> {
    private List<HttpFile> mHttpFileList = new ArrayList<HttpFile>();
    /**
     * parser
     */
    private UploadResponseParser mParser;

    /**
     * constructor
     */
    public HttpUploadRequest(Context context) {
        super(context);
    }

    @Override
    public Request build() {
        //default params
        if (appendDefaultParam) {
            appendDefaultParams();
        }

        if (appendDefaultHeader) {
            appendDefaultHeaders();
        }

        MultipartBody.Builder multipart = new MultipartBody.Builder();
        multipart.setType(MultipartBody.FORM);
        if (mParams != null && mParams.size() > 0) {
            for (String key : mParams.keySet()) {
                multipart.addFormDataPart(key, String.valueOf(mParams.get(key)));
            }
        }
        if (mHttpFileList.size() > 0) {
            try {
                for (HttpFile httpfile : mHttpFileList) {
                    if (httpfile == null || httpfile.file == null
                            || httpfile.file.exists() == false || httpfile.file.isDirectory()) {
                        continue;
                    }
                    String filePath = httpfile.file.getAbsolutePath();
                    String fileSimpleName = getFileSimpleName(filePath);
                    FileNameMap fileNameMap = URLConnection.getFileNameMap();
                    String mimeType = fileNameMap.getContentTypeFor(filePath);
                    if (mimeType == null) {
                        mimeType = "application/octet-stream";
                    }
                    HttpClient.instance().report("HttpUploadRequest", "====file mimetype:" + mimeType);
                    MediaType mediaType = MediaType.parse(mimeType);
                    multipart.addFormDataPart(httpfile.param == null ? "" : httpfile.param,
                            fileSimpleName == null ? "file" : fileSimpleName, RequestBody.create(mediaType, httpfile.file));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Request.Builder builder = new Request.Builder();
        if (mHeaders != null) {
            for (String key : mHeaders.keySet()) {
                builder.addHeader(key, mHeaders.get(key));
            }
        }
        builder.url(mUrl).post(multipart.build());
        return builder.build();
    }

    @Override
    public ResponseParser getParser() {
        if (mParser == null) {
            mParser = new UploadResponseParser(this);
        }
        return mParser;
    }

    /**
     */
    public HttpUploadRequest file(HttpFile httpfile) {
        mHttpFileList.clear();
        mHttpFileList.add(httpfile);
        return this;
    }

    /**
     */
    public HttpUploadRequest files(List<HttpFile> fileList) {
        if (fileList != null) {
            mHttpFileList.clear();
            mHttpFileList.addAll(fileList);
        }
        return this;
    }


    private String getFileSimpleName(String path) {
        if (path == null) {
            return null;
        }
        int lastsplash = path.lastIndexOf(File.separator);
        if (lastsplash == -1) {
            return path;
        }
        return path.substring(lastsplash + 1);
    }
}
