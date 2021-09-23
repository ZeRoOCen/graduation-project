package com.computermall.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.computermall.project.POJO.*;
import com.computermall.project.mapper.GoodMapper;
import com.computermall.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.GoodService;
import service.UserService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodService goodService;

//    @Autowired
//    private UserMapper userMapper;

//    @Autowired
//    private GoodMapper goodMapper;

    @Autowired
    @Qualifier("redisTemplate")  //由于我们自定义的redisTemplate与官方的重名了，所以一定要让配置的redisTemplate指向的是自己配置的
    private RedisTemplate redisTemplate;

    //查询用户界面所需的 用户总数/分页查询
    //由于前端使用get请求，所以不需要@RequestBody（post请求需要）
    @GetMapping("/alluser")
    public Object getUserList(QueryInfo queryInfo){
        //获取最大列表数(用户总数)。用户名进行模糊查询并进行int类型的数字存储。
        
        Integer numbers = userService.getUserCounts("%" + queryInfo.getQuery() + "%");
//        int numbers = userMapper.getUserCounts("%" + queryInfo.getQuery() + "%");
        //pageStart：页数从哪开始，即当前编号。 queryInfo.getPageSize())：每页最大存放数
        int pageStart = (queryInfo.getPageNum() - 1) * queryInfo.getPageSize();
        //获取每页的用户信息
        List<User> users = userService.getAllUser("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());
//        List<User> users = userMapper.getAllUser("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());

        HashMap<String, Object> res = new HashMap<>(); //使用HashMap函数存储数据（需要传送两个及两个以上的参数时）
        res.put("numbers",numbers);  //用户总数
        res.put("userData",users);  //用户列表，分页查询
        redisTemplate.opsForValue().set("res_json",res); //由于无法return返回多个参数（users + numbers），所以需要使用HashMap封装一下
        Object res_json = redisTemplate.opsForValue().get("res_json");
        return res_json;
    }


    //获取所有用户信息，取其中的头像绝对路径uavatar并返回
    @PostMapping("/useravatar")
    public Object getUserAvatar(@RequestBody String userList){

        ArrayList uavatarUrl = new ArrayList(); //定义动态数组，用于存储之后获取到的每一个uavatar路径

            //前端传过来的是JSON字符串对象格式，转换成JSON对象可以从中取值
        JSONObject userListComplexObj = JSONObject.parseObject(userList);

            //userListComplexObj.size()得到对象(键值对)的长度为5（0-4）
        for(int i=0; i<userListComplexObj.size(); i++ ){
                //getJSONObject(i)获取每一个键(i)的值，使得此题从字符串对象格式 -> 对象格式
                //Integer.toString(i), 由于getJSONObject()里面存放的必须是String类型不是int类型，这里将int类型强转成String类型
            JSONObject userListSimpleObj = userListComplexObj.getJSONObject(Integer.toString(i));
                //获取每一个对象里面的uavatar头像路径
            String avatarUrl = userListSimpleObj.getString("uavatar");
                // 如果头像路径不是""的或者null，则把头像路径加入动态数组中保存
            if(avatarUrl != null && avatarUrl != "") {
                uavatarUrl.add(avatarUrl);
                //如果是的话，就把""保存进动态数组（不需要null）
            }else{
                uavatarUrl.add("");
            }
        }

        redisTemplate.opsForValue().set("avatarUrl",uavatarUrl);
        Object avatarUrl = redisTemplate.opsForValue().get("avatarUrl");

        return avatarUrl;
    }

    //根据用户id查询此用户全部信息并返回全部信息
    @PostMapping("/getuserinfo")
    public Object getUserInfo(@RequestBody User user){
        int uid = user.getUid();
        User users = userService.getUserInfo(uid);
//        User users = userMapper.getUserInfo(uid);
        redisTemplate.opsForValue().set("res_json",users);
        Object userInfo = redisTemplate.opsForValue().get("res_json");
        return userInfo ;
    }

    //根据用户id查询此用户全部信息并返回头像路径
    @PostMapping("/getuseravatar")
    public Object getUserAvatar(@RequestBody User user){
        System.out.println("user===>" + user);
        int uid = user.getUid();
        User users = userService.getUserInfo(uid);
//        User users = userMapper.getUserInfo(uid);
        String avatarUrl = users.getUavatar();
        redisTemplate.opsForValue().set("avatarUrl",avatarUrl);
        Object uavatarUrl = redisTemplate.opsForValue().get("avatarUrl");
        return uavatarUrl ;
    }

    //修改用户状态   @RequestParam配合前端的put请求方式使用
    //使用put请求的形态：public String updateUserState(@RequestParam("userId")Integer uid, @RequestParam("userState")Integer ustate)
    //由于封装完axios后无法使用put，此处改为get请求
    @GetMapping("/userstate")
    public String updateUserState(Integer uid, Integer ustate){
        int judge = userService.updateUserState(uid,ustate);
//        int judge = userMapper.updateUserState(uid,ustate);
        return judge > 0 ? "success":"error";
    }

//  不使用API接口封装put请求时，需要使用@RequestParam配合
//    @PutMapping("/userstate")
//    public String updateUserState(@RequestParam("userId") Integer uid, @RequestParam("userState") Integer ustate){
//        System.out.println(uid);
//        System.out.println(ustate);
//        int judge = userMapper.updateUserState(uid, ustate); //判断：judge
//        return judge > 0 ? "success":"error";
//    }

    //修改用户名（个人中心-我的资料）
    @PostMapping("/updateuname")
    public Object updateUname(@RequestBody User user){
        String uName = user.getUname();
        int uId = user.getUid();
        int judge = userService.updateUname(uName,uId);
//        int judge = userMapper.updateUname(uName,uId);
        if(judge <= 0) return "error";

        int userId = user.getUid();
        User userInfo = userService.getUserInfo(userId);
//        User userInfo = userMapper.getUserInfo(userId);
        String uname = userInfo.getUname();

        redisTemplate.opsForValue().set("userName",uname);
        Object userName = redisTemplate.opsForValue().get("userName");
        return userName;
    }

    //修改用户姓别（个人中心-我的资料）
    @PostMapping("/updateusex")
    public Object updateSex(@RequestBody User user){
        int uSex = user.getUsex();
        int uId = user.getUid();
        int judge = userService.updateUsex(uSex,uId);
//        int judge = userMapper.updateUsex(uSex,uId);
        if(judge <= 0) return "error";

        int userId = user.getUid();
        User userInfo = userService.getUserInfo(userId);
//        User userInfo = userMapper.getUserInfo(userId);
        int usex = userInfo.getUsex();

        redisTemplate.opsForValue().set("userSex",usex);
        Object userSex = redisTemplate.opsForValue().get("userSex");

        return userSex;
    }

    //修改用户手机号（个人中心-我的资料）
    @PostMapping("/updateumobile")
    public Object updateUmobile(@RequestBody User user){
        String uMobile = user.getUmobile();
        int uId = user.getUid();
        int judge = userService.updateUmobile(uMobile,uId);
//        int judge = userMapper.updateUmobile(uMobile,uId);
        if(judge <= 0) return "error";

        int userId = user.getUid();
        User userInfo = userService.getUserInfo(userId);
//        User userInfo = userMapper.getUserInfo(userId);
        String umobile = userInfo.getUmobile();

        redisTemplate.opsForValue().set("userMobile",umobile);
        Object userMobile = redisTemplate.opsForValue().get("userMobile");
        return userMobile;
    }

    //修改用户收获地址（个人中心-我的资料）
    @PostMapping("/updateuaddress")
    public Object updateUaddress(@RequestBody User user){
        String uAddress = user.getUaddress();
        int uId = user.getUid();
        int judge = userService.updateUaddress(uAddress,uId);
//        int judge = userMapper.updateUaddress(uAddress,uId);
        if(judge <= 0) return "error";

        int userId = user.getUid();
        User userInfo = userService.getUserInfo(userId);
//        User userInfo = userMapper.getUserInfo(userId);
        String uaddress = userInfo.getUaddress();

        redisTemplate.opsForValue().set("userAddress",uaddress);
        Object userAddress = redisTemplate.opsForValue().get("userAddress");
        return userAddress;
    }

    //添加用户
    @PostMapping("/adduser")
    public String addUser(@RequestBody User user){
        user.setUstate(1); //后台添加的用户默认状态为1
        int judge = userService.addUser(user);
//        int judge = userMapper.addUser(user);
        return judge > 0 ? "success":"error";
    }

    //添加用户头像
        //System.out.println(file.toString()); value = org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@4803b674
        //file; value = org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@1f97562c
        //file.getBytes();  value = [B@ac7a8ab
        //file.getContentType();  value = image/jpeg
        //file.getInputStream();  value = java.io.FileInputStream@7a19d06e
        //file.getResource();  value = MultipartFile resource [file]
        //file.getSize();  value = 6033
        //file.isEmpty();  value = false
        //file.getClass();  value = class org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile
    @PostMapping("/uploadavatarurl")
    public String addUserAvatar(MultipartFile file) throws IOException {
        //获取头像原名     eg: file.getName() ==> value = file
        String avatarName = file.getOriginalFilename();// ===> value = imgName.jpg
            //获取头像原名的后缀，即图片格式   之前的写法：String ext = "." + avatarName.substring(avatarName.lastIndexOf(".")+1);
        String ext = avatarName.substring(avatarName.lastIndexOf("."));
            //定义存储用户头像的地址(父路径)  图片保存在前端的assets中
        String folder="D:/软件/码农项目/Vue项目/computermall/src/assets/userAvatar-image";
            //映射父文件目录路径（并未创建父文件目录路径）
        File avatarFolder = new File(folder);
            // 获取时间字符串
        String dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
            // 生成新的头像名（日期 + UUID自动生成唯一标识符 + 头像格式）  replaceAll("-","")：把存在的 '-' 替换成 ' '
        String newAvatarName = dateTimeFormatter + UUID.randomUUID().toString().replaceAll("-","") + ext;
            // 映射文件在本机的全路径（头像父路径 + 新生成的头像名）    File(String parent, String child) 父路径名字符串 和 文件本身命名
        File avatarPath = new File(avatarFolder, newAvatarName);
            //avatarPath = "D:/软件/码农项目/Vue项目/computermall/src/assets/userAvatar-image/2021031....faa.jpg"   全路径
            //avatarPath.getParentFile() = "D:/软件/码农项目/Vue项目/computermall/src/assets/userAvatar-image"      父路径
            //!avatarPath.getParentFile().exists()：当此图片的父级目录不存在时
        if (!avatarPath.getParentFile().exists()){
                //创建此图片的父级目录（真实创建，不是映射）（补全）
            avatarPath.getParentFile().mkdirs();
        }
        try{
                //上传文件到指定的文件夹中保存起来（avatarPath = 头像父路径 + 新生成的头像名）
            file.transferTo(avatarPath);
                //在这里做数据库上传操作
                    //先使用String类型拼接父路径和头像名
            String avatarPathString = folder + "/" + newAvatarName;
                    //获取到数据库中新添加的用户的id
            int userId = userService.getNewUserId();
//            int userId = userMapper.getNewUserId();
                    //根据新添加的用户的id，把头像传给它
            int judge = userService.updateUserAvatar(userId,avatarPathString);
//            int judge = userMapper.updateUserAvatar(userId,avatarPathString);
            String SEjudge = judge > 0? "success" : "error";
                // 返回之前生成的新的文件名   filePath.getName() — 获取文件本身命名    filePath.getParent()：获取父路径名字符串
            return "上传成功，图片命名为：" + avatarPath.getName() + "头像路径存入数据库中成功/失败：" + SEjudge;
        }catch (IOException e){
            e.printStackTrace();
            return "error";
        }
    }

    //修改用户头像
    @PostMapping("/updateavatar")
    public String updateAvatar(@RequestBody MultipartFile file, Integer userId){
        //获取头像原名
        String avatarName = file.getOriginalFilename();
        //获取头像原名的后缀，即图片格式   之前的写法：String ext = "." + avatarName.substring(avatarName.lastIndexOf(".")+1);
        String ext = avatarName.substring(avatarName.lastIndexOf("."));
        //定义存储用户头像的地址(父路径)  图片保存在前端的assets中
        String folder="D:/软件/码农项目/Vue项目/computermall/src/assets/userAvatar-image";
        //映射父文件目录路径（并未创建父文件目录路径）
        File avatarFolder = new File(folder);
        // 获取时间字符串
        String dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        // 生成新的头像名（日期 + UUID自动生成唯一标识符 + 头像格式）  replaceAll("-","")：把存在的 '-' 替换成 ' '
        String newAvatarName = dateTimeFormatter + UUID.randomUUID().toString().replaceAll("-","") + ext;
        // 映射文件在本机的全路径（头像父路径 + 新生成的头像名）    File(String parent, String child) 父路径名字符串 和 文件本身命名
        File avatarPath = new File(avatarFolder, newAvatarName);
        //avatarPath = "D:/软件/码农项目/Vue项目/computermall/src/assets/userAvatar-image/2021031....faa.jpg"   全路径
        //avatarPath.getParentFile() = "D:/软件/码农项目/Vue项目/computermall/src/assets/userAvatar-image"      父路径
        //!avatarPath.getParentFile().exists()：当此图片的父级目录不存在时
        if (!avatarPath.getParentFile().exists()){
            //创建此图片的父级目录（真实创建，不是映射）（补全）
            avatarPath.getParentFile().mkdirs();
        }
        try{
            //上传文件到指定的文件夹中保存起来（avatarPath = 头像父路径 + 新生成的头像名）
            file.transferTo(avatarPath);
            //在这里做数据库上传操作
            //先使用String类型拼接父路径和头像名
            String avatarPathString = folder + "/" + newAvatarName;
            //根据用户ID和新头像路径来修改对应的旧头像路径
            int judge = userService.updateUserAvatar(userId,avatarPathString);
//            int judge = userMapper.updateUserAvatar(userId,avatarPathString);
            String SEjudge = judge > 0? "success" : "error";
            // 返回之前生成的新的文件名   filePath.getName() — 获取文件本身命名    filePath.getParent()：获取父路径名字符串
            return avatarPath.getName();
        }catch (IOException e){
            e.printStackTrace();
            return "error";
        }
    }


    //根据用户ID查询此用户全部信息
    @GetMapping("/getupdateuser")
    public Object getUpdateUser(Integer uid){
        User user = userService.getUserInfo(uid);
//        User user = userMapper.getUserInfo(uid);
        redisTemplate.opsForValue().set("userInfo",user);
        Object userInfo = redisTemplate.opsForValue().get("userInfo");
        return userInfo;
    }

    //编辑用户信息
    @PostMapping("/edituser")
    public String editUser(@RequestBody User user){
        int judge = userService.editUser(user);
//        int judge = userMapper.editUser(user);
        return judge > 0 ? "success":"error";
    }

    //删除用户
    @DeleteMapping("/deleteuser")
    public String deleteUser(Integer uid){
        int judge = userService.deleteUser(uid);
//        int judge = userMapper.deleteUser(uid);
        return judge > 0 ? "success":"error";
    }

    //根据用户名和商品客服名判断备用联系客服表中是否存在该用户对此商品的唯一id，若不存在，则添加唯一id以及添加对应的用户名和商品客服名，最后返回它们的值
    @PostMapping("/idexistjudge")
    public Object idExistJudge(@RequestBody GnameAndUname gnameAndUname){
        //根据商品名获取到对应的商品客服名
        String cname = goodService.selectCustomerName(gnameAndUname.getGname());
//        String cname = goodMapper.selectCustomerName(gnameAndUname.getGname());
        //判断是否存在用户对应的商品客服名的唯一id，如果不存在，则添加唯一id；如果存在，返回存在
        Integer existJudge = userService.selectConnectionId(cname, gnameAndUname.getUname());
//        Integer existJudge = userMapper.selectConnectionId(cname, gnameAndUname.getUname());
        if(existJudge == null){
            //随机六位数(100000 ~ 999999)
            Random r = new Random();
            int random = (r.nextInt(899999)+100000);
            String randomSix = Integer.toString(random);

//Integer addJudge = userMapper.addContactInfo(cname, gnameAndUname.getUname(), onlyId);无法识别数字与字母组合的字符串
//            //随机生成三个大写字母
//            String rdLetterFirst = "";
//            for(int i=0; i<3; i++){
//                char LetterFirst = (char)(Math.random()*26 + 65);
//                rdLetterFirst += String.valueOf(LetterFirst);
//            }

            String onlyId = randomSix;

            //根据用户名和商品客服名添加唯一ID
            Integer addJudge = userService.addContactInfo(cname, gnameAndUname.getUname(), onlyId);
//            Integer addJudge = userMapper.addContactInfo(cname, gnameAndUname.getUname(), onlyId);
            if(addJudge != 1)
                return "添加唯一ID失败";
        }
        //获取该用户名的所有联系客服信息
        List<SpareCustomer> spareCustomer = userService.selectContactInfo(gnameAndUname.getUname());
//        List<SpareCustomer> spareCustomer = userMapper.selectContactInfo(gnameAndUname.getUname());
        redisTemplate.opsForValue().set("Customer", spareCustomer);
        Object Customer = redisTemplate.opsForValue().get("Customer");
        return Customer;
    }

        //获取该用户的所有联系客服信息
        @PostMapping("/getcustomerinfo")
        public Object getCustomerInfo(@RequestBody Uname uname){
            List<SpareCustomer> spareCustomer = userService.selectContactInfo(uname.getUname());
//            List<SpareCustomer> spareCustomer = userMapper.selectContactInfo(uname.getUname());
            redisTemplate.opsForValue().set("Customer", spareCustomer);
            Object Customer = redisTemplate.opsForValue().get("Customer");
            return Customer;
        }

        //获取客服头像并返回给前端
//        @PostMapping("/getavatar")
//        public String getCustomerAvatar(@RequestBody CustomerName customerName){
//            String cname = customerName.getCname();
//            String avatar = userMapper.getCustomerAvatar(cname);
//
//            return avatar;
//        }

        //生成发送消息的时间并保存所有消息信息
    @PostMapping("/sendchatmessage")
    public String saveChatInfo(@RequestBody IdNameAndMessage idNameAndMessage){
        String id = idNameAndMessage.getId();
        String name = idNameAndMessage.getName();
        String message = idNameAndMessage.getMessage();

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); //生成当前时间

        Integer saveJudge = userService.saveChatInfo(id, name, message, time);
//        Integer saveJudge = userMapper.saveChatInfo(id, name, message, time);

        return saveJudge > 0 ? "success" : "error";
    }

        //获取到指定连接id的唯一消息id，发送名称，发送信息和发送时间
    @PostMapping("/getchatmessage")
    public Object getChatMessage(@RequestBody ConnectId connectId){
        String id = connectId.getConnectid();
        List<SpareChatInfo> spareChatInfo = userService.getChatMessage(id);
//        List<SpareChatInfo> spareChatInfo = userMapper.getChatMessage(id);

        redisTemplate.opsForValue().set("info", spareChatInfo);
        Object resInfo = redisTemplate.opsForValue().get("info");
        return resInfo;

    }

        //客服获取用户列表
    @PostMapping("/getuserlist")
    public Object getUserList(@RequestBody Cname cname){
        String customername = cname.getCname();

        List<UnameAndId> unameAndId = userService.getUserList(customername);
//        List<UnameAndId> unameAndId = userMapper.getUserList(customername);

        redisTemplate.opsForValue().set("userNameAndConnectid", unameAndId);
        Object userNameAndConnectid = redisTemplate.opsForValue().get("userNameAndConnectid");

        return userNameAndConnectid;
    }

}
