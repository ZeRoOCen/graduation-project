package com.computermall.project.controller;
import com.computermall.project.mapper.GoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
public class ImgUploadController {

    @Autowired
    GoodMapper goodMapper;

    @Autowired
    @Qualifier("redisTemplate")  //由于我们自定义的redisTemplate与官方的重名了，所以一定要让配置的redisTemplate指向的是自己配置的
    private RedisTemplate redisTemplate;

//上传图片到指定的本地文件夹中
    @PostMapping("/uploadurl")
    //MultipartFile file：文件格式接收前端传过来的参数
    public String imgUpload(MultipartFile file) {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
            //substring(start,stop)
                //start 必需。一个非负的整数，规定要提取的子串的第一个字符在 stringObject 中的位置。字符串起始位是 0 而不是 1
                //stop 可选。一个非负的整数，比要提取的子串的最后一个字符在 stringObject 中的位置多1。如果省略该参数，那么返回的子串会一直到字符串的结尾。
            //lastIndexOf(searchvalue,fromindex) 方法可返回一个指定的字符串值最后出现的位置，在一个字符串中的指定位置从后向前搜索
                //searchvalue 	必需。规定需检索的字符串值。
                //fromindex 可选的整数参数。规定在字符串中开始检索的位置。它的合法取值是 0 到 length - 1。如省略，则将从字符串的最后一个字符处开始检索。
        String ext = "." + fileName.substring(fileName.lastIndexOf(".")+1);

        // 定义存储图片的地址(父路径)   此题无法使用相对定位，只能用绝对定位（当然可能通过设计复杂的绝对定位规则来达到相对定位的程度）
        String folder = "D:/软件/码农项目/Vue项目/computermall/src/assets/good-image";

        // 映射文件夹路径(存储图片的文件父目录映射)
            //new File("路径")  只是在内存中创建File文件映射对象,而并不会在硬盘中创建文件
            //判断映射的文件是否真实存在     file.exists()  //true存在  false不存在
            //如果存在即可直接操作, 否则需要调用 file.createNewFile() 创建真实文件
            //但是以上方式只会适用创建文件本身,不包括父文件的创建(如果父文件不存在)
            //所以一般需要对父文件存在与否作判断并创建包括父文件目录在内的全路径
            //File parent = file.getParentFile()  // 获取父文件
            //if( !parent.exists() )  parent.mkdirs();  //创建所有父文件夹
        File imgFolder = new File(folder);

        // 获取时间字符串
            //DateTimeFormatter.ofPattern("yyyyMMddHHmmss")：指定日期/时间模式为yyyyMMddHHmmss
            //LocalDateTime.now()：获取当前时间戳方式之一
            //format(LocalDateTime.now())：LocalDateTime.now()时间戳转成String类型的时间
        String dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());

        // 生成新的文件名（日期+UUID自动生成唯一标识符）  replaceAll("-","")：把存在的 '-' 替换成 ' '
        String newFileName = dateTimeFormatter + UUID.randomUUID().toString().replaceAll("-","") + ext;

        // 映射文件在本机的全路径（包括自身的新命名）    File(String parent, String child) 父路径名字符串 和 文件本身命名
        File filePath = new File(imgFolder, newFileName);

        //filePath = "D:/软件/码农项目/Vue项目/computermall/src/assets/good-image/2021031....faa.jpg"
        //filePath.getParentFile() = "D:/软件/码农项目/Vue项目/computermall/src/assets/good-image"
        //!filePath.getParentFile().exists()：当此图片的父级目录不存在时
        if (!filePath.getParentFile().exists()){
        //创建此图片的父级目录（补全）
            filePath.getParentFile().mkdirs();
        }
        try{
            //上传文件到指定的文件夹中保存起来（文件路径为filePath中的imgFolder父路径名，文件名为newFileName文件本身命名）
            file.transferTo(filePath);

            //在这里做数据库上传操作


            // 返回之前生成的新的文件名   filePath.getName() — 获取文件本身命名    filePath.getParent()：获取父路径名字符串
            return "上传成功，图片命名为：" + filePath.getName();
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }




}


