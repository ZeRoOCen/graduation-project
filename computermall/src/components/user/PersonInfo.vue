<template>
  <div>
    <section>
      <!-- 上传头像
              :action="uploadAvatarUrl"    上传文件的后端全路径地址（必填）  :action="uploadUrl"  uploadAvatarUrl:"http://localhost:9000/uploadavatarurl"
              :limit="limit"：    单次最大允许上传个数
              :auto-upload="false"：  取消自动上传
              ref="avatarUpload"：    给avatarUpload绑定上此el-upload组件的方法
              before-upload(file)：   上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise(this.$confirm相关) 且被 reject，则停止上传。
              before-remove(file, fileList)： 删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除。
              on-preview(file)：              点击文件列表中已上传的文件时的钩子
              on-success(file, fileList)：    文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
              on-remove(file, fileList)：	    文件列表移除文件时的钩子
              on-change(file,fileList):       文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
              on-exceed(file, fileList)：     文件超出个数限制时的钩子
              on-error(err, file, fileList)： 文件上传失败时的钩子-->
      <el-upload
        class="avatar-uploader"
        action="useHttpRequest"
        :http-request="uploadAvatar"
        :limit="limit"
        ref="avatarUpload"
        :before-upload="beforeAvatarUpload"
        :before-remove="beforeAvatarRemove"
        :on-preview="handleAvatarPreview"
        :on-success="handleAvatarSuccess"
        :on-remove="handleAvatarRemove"
        :on-exceed="handleAvatarExceed"
        :on-error="handleAvatarError"
      >
        <div class="avatar_border">
          <!-- :v-show="defaultAvatarShow"-->
          <!-- 用户登录时的默认头像 -->
          <img
            :src="defaultImageUrl"
            class="avatar"
          />
          <!-- 修改后的新用户头像 -->
          <!-- <img v-show="newAvatarShow" v-if="newAvatarShow == true" :src="newAvatarUrl" class="avatar" /> -->
          
          <!-- 虚线边框 -->
          <!-- <i
            class="el-icon-plus avatar-uploader-icon"
          ></i> -->
        </div>
      </el-upload>

      <!-- 身份 -->
      <el-form
        ref="uroleFormRef"
        :model="personInfo"
        label-width="95px"
        size="small"
      >
        <el-form-item prop="urole">
          <el-badge v-if="personInfo.urole == 1" value="升级">
            <el-button v-if="personInfo.urole == 1" class="personInfo_urole_1">
              普通用户
            </el-button>
          </el-badge>
          <el-badge v-if="personInfo.urole == 2" value="尊贵">
            <el-button v-if="personInfo.urole == 2" class="personInfo_urole_2">
            会员用户
            </el-button>
          </el-badge>
          <div v-if="personInfo.urole == 11">查询管理员</div>
          <div v-if="personInfo.urole == 12">查询+权限控制管理员</div>
        </el-form-item>
      </el-form>

      <!-- 用户名 -->
      <el-form
        ref="unameFormRef"
        :model="personInfo"
        :rules="unameRule"
        label-width="150px"
        size="small"
      >
        <el-form-item label="用户名" prop="uname" class="el_form_commonItem">
          <el-input
            class="userInfo_commonInput"
            v-model="personInfo.uname"
          >
          </el-input>
          <el-button  class="el-icon-check personInfo_submitButton_common" @click="updateUname"></el-button>
        </el-form-item>
      </el-form>

      <!-- 姓别 -->
      <el-form
        ref="usexFormRef"
        :model="personInfo"
        :rules="usexRule"
        label-width="150px"
        size="small"
      >
        <el-form-item label="姓别" prop="usex" class="el_form_commonItem">
          <el-radio-group v-model="personInfo.usex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
          <el-button class="el-icon-check personInfo_submitButton_common" @click="updateUsex"></el-button>
        </el-form-item>
      </el-form>

      <!-- 手机号 -->
      <el-form
        ref="umobileFormRef"
        :model="personInfo"
        label-width="150px"
        :rules="umobileRule"
        size="small"
      >
        <el-form-item label="手机号" prop="umobile" class="el_form_commonItem">
          <el-input
            class="userInfo_commonInput"
            v-model="personInfo.umobile"
          ></el-input>
          <el-button  class="el-icon-check personInfo_submitButton_common" @click="updateUmobile"></el-button>
        </el-form-item>
      </el-form>

      <!-- 收货地址 -->
      <el-form
        ref="uaddressFormRef"
        :model="personInfo"
        label-width="150px"
        :rules="uaddressRule"
        size="small"
      >
        <el-form-item label="收货地址" prop="uaddress">
          <el-input
            class="userInfo_input_address"
            v-model="personInfo.uaddress"
          ></el-input>
          <el-button  class="el-icon-check personInfo_submitButton_common" @click="updateUaddress"></el-button>
        </el-form-item>
      </el-form>
    </section>
  </div>
</template>

<script>
// import axios from "axios";

// axios.create({
//   baseUrl: "http://localhost:9000/", //请求地址
// });

export default {
  name: "personInfo",

  created() {
  },
  mounted() {
    this.defaultUserInfo(); //用户信息默认展示
    this.getUserAvatar(); //获取用户头像（登录时的默认头像）
  },
  updated(){

  },
  data() {
    return {
      personInfo:{}, //获取到的用户信息
      limit: 1,

      // defaultAvatarShow: true,
      // img:"",
      defaultImageUrl: require( "@/assets/userAvatar-image/" + this.getUserAvatar()),
      imageUrl:"",

      // newAvatarShow: false,
      // newAvatarUrl: require("@/assets/userAvatar-image/" + this.avatarSuffixUrl), //新用户头像

//用户独立表单信息验证
      unameRule:{
        uname:[
          //required是否为必填项；
          //message当内容不符合规范(必填项没信息或者长度不对)时展示的信息内容；
          //trigger:'blur / change' 失去焦点时触发 / 数据改变时触发
          { required: false, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 12, message: '长度在 2 到 12 个字符', trigger: 'change' }
        ],
      },
      usexRule:{
        usex:[
          //required是否为必填项；
          //message当内容不符合规范(必填项没信息或者长度不对)时展示的信息内容；
          //trigger:'blur / change' 失去焦点时触发 / 数据改变时触发'
          { required: false, message: '请选择姓别', trigger: 'blur' },
          // { min: 2, max: 12, message: '长度在 2 到 12 个字符', trigger: 'change' }, 姓别不需要这个
        ],
      },
      umobileRule:{
        umobile:[
          //required是否为必填项；
          //message当内容不符合规范(必填项没信息或者长度不对)时展示的信息内容；
          { required: false, message: '请输入手机号', trigger: 'blur' },
          { min: 2, max: 14, message: '长度在 2 到 14 个字符', trigger: 'change' },
        ],
      },
      uaddressRule:{
        uaddress:[
          //required是否为必填项；
          //message当内容不符合规范(必填项没信息或者长度不对)时展示的信息内容；
          { required: false, message: '请输入收获地址', trigger: 'blur' },
          { min: 2, max: 64, message: '长度在 2 到 64 个字符', trigger: 'change' },
        ],
      },

    }
  },

  methods: {
    //根据登录的用户信息（存放在浏览器中）默认展示在输入框中（用户信息修改操作无法使用此方法）
    async defaultUserInfo() {
      const getData = window.localStorage.getItem("loginUserInfo");
      // 保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型才可以使用
      const getUserInfo = JSON.parse(getData);

      //如果用户信息更新过，重新刷新时，就不能直接获取存放在浏览器中的过时的用户信息，需要根据用户ID（不会改变的数据）来查询最新的用户信息
      const res = await this.$http.getuserinfo( {...getUserInfo} );
      this.personInfo = res;
    },

    //从数据库中根据用户ID查询此用户全部信息
    async userInfo(){
      // const uid = this.personInfo.uid;  无法直接返回uid，因为他不是JSON字符串类型的
      const res = await this.$http.getuserinfo( {...this.personInfo} );
      this.personInfo = res;  
    },

    // 用户已有头像默认显示
    getUserAvatar() {
      //获取保存在浏览器上的用户信息
      const getLocalData = window.localStorage.getItem("loginUserInfo");
      //保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型就可以通过打点方式取出自己想要的值
      const getUserAvatar = JSON.parse(getLocalData);
      const avatarUrl = getUserAvatar.uavatar; //取出用户头像的绝对路径
      // console.log("avatarUrl===>" , avatarUrl)
      
      // 返回数据库中的头像名称+后缀
      // substring(start,end)获取start-end索引值之间的字符串（包括start索引值，不包括end索引值），如果只有start，则从start到结尾
      // lastIndexOf（从最后一个此字符串开始索引）
      // const res = await this.$http.getuseravatar( {...getUserAvatar} );
      // console.log("res===>",res)
      // return res.substring(res.lastIndexOf("/") + 1);
      return avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
    },

    //用户名修改
    updateUname(){
      this.$refs.unameFormRef.validate(async valid => {
        if(!valid) return ;

        const res = await this.$http.updateuname( {...this.personInfo} );
        if(res == "error") return this.$message.error("修改用户名失败");
        this.$message.success("信息修改成功");
        this.personInfo.uname = res;
        this.userInfo(); //用户信息更新成功，页面重新获取此用户最新的用户信息并显示在输入框中
      })
    },

    //用户姓别修改
    updateUsex(){
      this.$refs.unameFormRef.validate(async valid => {
        if(!valid) return ;

        const res = await this.$http.updateusex( {...this.personInfo} );

        if(res == "error") return this.$message.error("修改用户名失败");
        this.$message.success("信息修改成功");
        this.personInfo.usex = res;
        this.userInfo(); //用户信息更新成功，页面重新获取此用户最新的用户信息并显示在输入框中
      })
    },

    //用户手机号修改
    updateUmobile(){
      this.$refs.umobileFormRef.validate(async valid => {
        if(!valid) return ;

        const res = await this.$http.updateumobile( {...this.personInfo} );
        if(res == "error") return this.$message.error("修改用户名失败");
        this.$message.success("信息修改成功");
        this.personInfo.umobile = res;
        this.userInfo(); //用户信息更新成功，页面重新获取此用户最新的用户信息并显示在输入框中
      })
    },

    //用户收获地址修改
    updateUaddress(){
      this.$refs.uaddressFormRef.validate(async valid => {
        if(!valid) return ;

        const res = await this.$http.updateuaddress( {...this.personInfo} );
        if(res == "error") return this.$message.error("修改用户名失败");
        this.$message.success("信息修改成功");
        this.personInfo.uaddress = res;
        this.userInfo(); //用户信息更新成功，页面重新获取此用户最新的用户信息并显示在输入框中
      })
    },


      //自定义上传头像
    async uploadAvatar(param) {

      //获取保存在浏览器上的用户信息
      const getLocalData = window.localStorage.getItem("loginUserInfo");
      //保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型就可以通过打点方式取出自己想要的值
      const getUserAvatar = JSON.parse(getLocalData);
      const userId = getUserAvatar.uid; //取出用户的id

      //http.js封装后的formData格式：  后端接收名(file): 前端传递名(param.file)
      //true为开启formData传递（默认为false，不使用formData传递）
      //由于传递的是文件类型的数据，所以要修改header请求头类型为文件类型
      //如果需要传递多个参数，只需要逗号","隔开就行
      const res = await this.$http.updateavatar({
        file: param.file,
        userId: userId
      },true,{
        headers: { "Content-Type": "multipart/form-data" }
      });

      //获取数据库的数据，再赋给window.localStorage.getItem("loginUserInfo")
      this.userInfo();
      const personInfo = JSON.stringify(this.personInfo);
      window.localStorage.setItem("loginUserInfo", personInfo);
      // if(res){
      //   this.defaultImageUrl = res
      // } else {
      //   alert('xxxxx');
      // }
      
      // if(!res) return;
      // this.defaultAvatarShow = false;
      // this.newAvatarShow = true;
      return res; //X后缀名传递给data中的newAvatarUrl中进行拼接成完整的头像路径
    
      //X下面是使用formdata进行存储数据，不过无需封装成formData，因为在http.js中已经封装了formData
      // const paramFile = param.file;
      // const formData = new FormData();
      // console.log(formData);
      // formData.append("imgFile", paramFile);
      // formData.append("userId", userId);
      //api封装后，上传的formData无参数，没有入参
      // const res = this.$http.updateavatar(
      //   { ...formData },
      //   { headers: { "Content-Type": "multipart/form-data" } }
      // );
    },

    // 👇添加文件(头像、图片)所需要的方法👇
    
    //上传文件之前的钩子，进行头像大小和格式的判断
    beforeAvatarUpload(file) {
      const fileName = file.name; //文件名
      const imgSuffix = fileName.substring(fileName.lastIndexOf(".") + 1); //获取文件后缀名
      const suffixArray = ["jpg", "png", "jpeg", "gif", "bmp"]; //限制的文件类型，根据情况自己定义
      if (suffixArray.indexOf(imgSuffix) === -1) {
        return this.$message.error("不支持此格式的文件，请上传图片格式的文件");
      }

      const isLt5M = file.size / 1024 / 1024 < 5;
      if (!isLt5M) {
        return this.$message.error("上传头像图片大小不能超过 5MB!");
      }
    },

    //上传成功时的钩子
    handleAvatarSuccess(response, file, fileList) {
      this.imageUrl = URL.createObjectURL(file.raw); //上传成功时，把上传的头像回显给前端
      console.log("response", response);
      console.log("file", file);
      console.log("fileList", fileList);
    },

    //删除文件之前的钩子（加上一个功能：若删除文件成功，则把file文件、fileList文件列表和this.imageUrl回显的头像展示都给删除掉）
    //需要进行async await异步处理，等待this.$confirm(`确定移除 ${file.name}？`)的返回值出来，judge才去获取
    //否则，只要执行了删除事件，judge直接就获取到了this.$confirm(`确定移除 ${file.name}？`)的值，而不是获取到它的返回值
    async beforeAvatarRemove(file, fileList) {
      const judge = await this.$confirm(`确定移除 ${file.name}？`);
      if (judge) {
        this.imageUrl = "";
      }
    },

    //文件列表移除文件时的钩子
    handleAvatarRemove(file, fileList) {
      console.log(file, fileList);
    },

    //点击文件列表中已上传的文件时的钩子
    handleAvatarPreview(file) {
      console.log(file);
    },

    //文件超出个数限制时的钩子
    handleAvatarExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，
        本次选择了 ${files.length} 个文件，
        共选择了 ${files.length + fileList.length}个文件`
      );
    },

    //文件上传失败时的钩子
    handleAvatarError(error, file, fileList) {
      this.imageUrl = "";
      console.log(
        "error===>" +
          error +
          "---file===>" +
          file +
          "---fileList===>" +
          fileList
      );
    },
  },
};
</script>

<style scoped>

/* 头像 */
.avatar_border {
  border: 1px dashed #000000;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar_border:hover {
  border-color: #409eff;
}

.avatar-uploader{
  width:100%;
  margin-top:20px;
  margin-bottom:30px;
  margin-left:220px;
}

.avatar {
  width: 178px;
  height: 178px;
}

.avatar-uploader-icon {
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  font-size: 28px;
  color: #8c939d;
}

/* 用户等级身份 */
.personInfo_urole_1 {
  width: 80px;
  border-radius: 10%;
  color: rgb(240, 240, 240);
  font-weight: bold;
  text-align: center;
  background-color: rgb(6, 179, 0);
}

.personInfo_urole_2{
  width: 80px;
  border-radius: 10%;
  color: rgb(240, 240, 240);
  font-weight: bold;
  text-align: center;
  background-color: rgb(225, 106, 255);
}

/* 用户通用表单信息 */
.el_form_commonItem {
  width: 500px;
}

/* 用户信息通用输入框样式 */
.userInfo_commonInput {
  width: 190px;
}

/* 用户信息地址输入框样式 */
.userInfo_input_address {
  width: 350px;
}

/* 用户信息提交通用按钮 */
.personInfo_submitButton_common{
  background-color: rgb(255, 255, 249);
  margin-left:15px;
}

</style>