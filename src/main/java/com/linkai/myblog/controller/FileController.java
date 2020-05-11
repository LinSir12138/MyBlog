package com.linkai.myblog.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController     // 不仅视图解析器，直接返回字符串
public class FileController {


    @RequestMapping("/uploadQNY")
    public JSONObject fileUploadQNY(@RequestParam("editormd-image-file") MultipartFile file, HttpServletRequest request) {

        // 注意是在 ：com.qiniu.storage 包下面的
        Configuration cfg = new Configuration(Region.region2());        // 由于我们选择的是华南的机房，所以这里调用 region2()

        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "";
        String secretKey = "";
        String bucket = "jacklin-blog";     // 填写我们的存储空间的名称


        //默认不指定key的情况下，以文件内容的hash值作为文件名
        // 我们这里的 key 可以指定为 本地文件的文件名
        String key = file.getOriginalFilename();

        // Edit.md  构造返回的 Json 字符串
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "http://image.linkaiblog.top/" + key);
        jsonObject.put("message", "upload success!");

        //如果文件名为空，直接回到返回
        if ("".equals(key)){
            // 0 表示上传失败
            jsonObject.put("success", 0);
            return jsonObject;
        }

        try {
            byte[] uploadButes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            Response response = uploadManager.put(uploadButes, key, upToken);
            // 解析上传成功的结果  --》 需要用到 com.google.gson.Gson
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println("文件名为：" + putRet.key);
            System.out.println("文件内容的hash值为" + putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 1 表示上传成功
        jsonObject.put("success", 1);
        return jsonObject;
    }

    @RequestMapping("/upload")
    public JSONObject fileUpload(@RequestParam("editormd-image-file") MultipartFile file , HttpServletRequest request) throws IOException {

        //获取文件名 : file.getOriginalFilename();
        String uploadFileName = file.getOriginalFilename();
        System.out.println("文件名为：");

        // 构造返回的 json 字符串
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", request.getContextPath() + "/upload/" + uploadFileName);
        jsonObject.put("message", "upload success!");



        //如果文件名为空，直接回到首页！
        if ("".equals(uploadFileName)){
            // 0 表示上传失败
            jsonObject.put("success", 0);
            return jsonObject;
        }
        System.out.println("上传文件名 : "+uploadFileName);

        //上传路径保存设置   UUID
        String path = request.getServletContext().getRealPath("/upload");
        //如果路径不存在，创建一个
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        System.out.println("上传文件保存地址："+realPath);

        InputStream is = file.getInputStream(); //文件输入流
        OutputStream os = new FileOutputStream(new File(realPath,uploadFileName)); //文件输出流

        //读取写出
        int len=0;
        byte[] buffer = new byte[1024];
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
            os.flush();
        }
        os.close();
        is.close();

        // 1 表示上传成功
        jsonObject.put("success", 1);
        return jsonObject;
    }



    /*
     *  第2中方式： 采用file.Transto 来保存上传的文件
     */
    @RequestMapping("/upload2")
    public String  fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        //上传文件地址
        System.out.println("上传文件保存地址："+realPath);

        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));

        return "redirect:/index.jsp";
    }

    // 图片下载
    @RequestMapping(value="/download")
    public String downloads(HttpServletResponse response , HttpServletRequest request) throws Exception{
        //要下载的图片地址
        String  path = request.getServletContext().getRealPath("/upload");
        String  fileName = "学习进度.png";

        //1、设置response 响应头
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        response.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头
        response.setHeader("Content-Disposition",
                "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));

        File file = new File(path,fileName);
        //2、 读取文件--输入流
        InputStream input=new FileInputStream(file);
        //3、 写出文件--输出流
        OutputStream out = response.getOutputStream();

        byte[] buff =new byte[1024];
        int index=0;
        //4、执行 写出操作
        while((index= input.read(buff))!= -1){
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
        return null;
    }
}
