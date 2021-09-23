<template>
  <div>
    <!-- 面包屑：显示当前页面的路径，快速返回之前的任意页面 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>信息查询管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户信息列表</el-breadcrumb-item>
      <el-breadcrumb-item>用户信息</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 用户列表主体
        el-card：将信息聚合在卡片容器中展示 
        slot="append"：黏着在父标签(此题为<el-input>标签)的最右侧   slot="prepend"：黏着在父标签的最左侧
        gutter：指定栏与栏之间的间隔，默认为零
        span：规定 col 元素应该横跨的列数
        offset：分栏偏移
        icon：图标样式
        开启flex布局：< el-row type="flex" justify="start/center/end/space-between（左右不留间隔）/space-around （左右也有间隔）">  
                     其中的值来定义子元素的排版方式 -->
    <el-card>
      <el-row :gutter="25">
        <el-col :span="10" :offset="6">
          <!-- v-model="queryInfo.query"：把用户输入的搜索内容与需要经过模糊查询的用户名进行双向绑定 -->
          <!-- clearable：在input输入框中增加一个删除小按钮。 -->
          <!-- @clear="getUserList"：当清除input输入框内容时，通过clear删除事件来触发getUserList()函数 -->
          <!-- @input = "getUserList"：当输入框发生变化时，触发getUserList函数 -->
          <!-- @click="getUserList"：通过click点击事件触发getUserList()函数，把用户输入的搜索内容通过getUserList传递给后端进行校验再返回回来 -->
          <el-input placeholder="请输入搜索内容"
            class="search_input"
            v-model="queryInfo.query"
            clearable
            @clear="getUserList"
            @input = "getUserList"
          >
            <!-- 搜索/查询按钮 -->
            <el-button slot="append"
              icon="el-icon-search"
              @click="getUserList"
            ></el-button>
          </el-input>
        </el-col>
        
        <!-- 添加用户按钮 
                @click=" addDialogVisible=true " 当点击按钮时，addDialogVisible=true显示添加用户对话框
                此判定与编辑用户信息对话框中的:visible.sync="addDialogVisible"进行匹配-->
        <el-col :span="4">
          <el-button type="primary" @click=" addDialogVisible=true ">添加用户</el-button>
        </el-col>
      </el-row>

      <!-- 对用户列表进行数据渲染 
              border：增加默认边框样式
              stripe：增加默认隔行换色样式
              v-bind:data = "userList"：绑定data中的userList，子标签可以通过prop来获取到userList里面的属性的值
              prop：单向绑定。当父组件的属性变化时，将传值给子组件-->
      <el-table class="userList_el_table" :data="userList" border stripe :header-cell-style="{textAlign: 'center'}" :cell-style="{ textAlign: 'center' }">
        <el-table-column label="ID" prop="uid" width="60px"></el-table-column>
        <el-table-column label="用户名" prop="uname" width="80px"></el-table-column>
        <el-table-column label="性别" prop="usex" width="60px">
          <template v-slot="usexList">
            <span v-if=" usexList.row.usex == 0 ">男</span>
            <span v-else>女</span>
          </template>
        </el-table-column>
        <el-table-column label="头像" prop="uavatar" width="130px">
        </el-table-column>
        <el-table-column label="角色权限" prop="urole" width="110px">
          <template v-slot="uroleList">
            <span v-if=" uroleList.row.urole == 1 ">普通用户</span>
            <span v-else-if=" uroleList.row.urole == 2 ">会员用户</span>
            <span v-else-if=" uroleList.row.urole == 11 ">查询管理员</span>
            <span v-else-if=" uroleList.row.urole == 12 ">查询+权限控制管理员</span>
            <span v-else-if=" uroleList.row.urole == 13 ">客服</span>
            <span v-else-if=" uroleList.row.urole == 19 ">超级管理员</span>
          </template>
        </el-table-column>
        <!--角色状态滑块判定  
              v-slot：作用域插槽；父组件可以调用子组件的数据；v-slot只能用在template上
              @change：当输入框失焦的时候触发. @change="userStateChanged(scope.row)"
              :active-value="1"：激活状态。其中的1是后台传递过来的state的值(int类型)
              :inactive-value="0"：关闭状态。同上 -->
        <el-table-column label="角色状态" prop="ustate" width="80px">
          <template v-slot="slotProps">
            <el-switch v-model="slotProps.row.ustate"
              @change="userStateChanged(slotProps.row)"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
            >
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column label="手机号" prop="umobile" width="110px"></el-table-column>
        <el-table-column label="地址" prop="uaddress" width="430px"></el-table-column>
        
        <!-- 编辑用户/删除用户/角色权限管理 -->
        <el-table-column label="操作">
          <!-- v-slot：作用域插槽；父组件可以调用子组件的数据；v-slot只能用在template上 -->
          <template v-slot="slotProps">
            <!-- 编辑/修改按钮 
                  el-tooltip文字提示：常用于展示鼠标 hover 停留在标签上时的显示的提示信息。
                    effect：tooltip容器的背景颜色，有dark和light两种颜色
                    content：文字提示内容
                    placement：content提示内容的位置
                    :enterable鼠标是否可进入到tooltip中，默认为true
                    offset="1" ：偏移量，正数向左，负数向右

                  这里不需要当点击时打开编辑窗口@click=" editDialogVisible=true "，因为后端async showEditDialog(uid)有打开窗口的事件
                  @click=" editDialogVisible=true " 当点击按钮时， editDialogVisible=true显示对话框
                      此判定与编辑用户信息对话框中的:visible.sync="editDialogVisible"进行匹配
                  @keyup.left：鼠标左键松开时触发-->
            <el-tooltip effect="dark" content="编辑用户" placement="top-end" :enterable="false" offset="1">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(slotProps.row.uid)"></el-button>
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip effect="dark" content="删除用户" placement="top" :enterable="false">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteUser(slotProps.row.uid)"></el-button>
            </el-tooltip>
            <!-- 分配权限按钮(未开发) -->
            <el-tooltip effect="dark" content="待开发功能" placement="top-start" :enterable="false" offset="-1">
              <el-button type="warning" icon="el-icon-setting" size="mini"
              ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

<!-- 分页查询
        @size-change： 每页最大展示数
        @current-change：使用事件来处理-当前页-变动时候触发的事件
        :current-page="queryInfo.pageNum" : 默认当前页显示
        page-sizes：接受一个整型数组，数组元素为展示的选择每页显示个数的选项
        :page-size：默认展示的每页最大数量
        :total="total": 最大展示页数（用户总数对应着最大页数，所以total对应着numbers用户总数：this.total = res.numbers）
        layout：组件。"total, sizes, prev, pager, next, jumper"都是element已经封装好的一些组件
-->
      <div>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.pageNum"
          :page-sizes="[1, 2, 5, 10]"
          :page-size="queryInfo.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
        >
        </el-pagination>
      </div>
    </el-card>

<!-- 添加用户对话框 
        :visible.sync：el-dialog特有属性
        :visible 选择器选取每个当前是可见的元素。指的是属性绑定，表示弹框的显示隐藏（元素是否可见），
            当:visible的值为ture的时候，弹框显示（元素可见），当为false的时候，弹框隐藏（元素不可见），
        .sync的意思是双向绑定visible的值，当我们关闭窗口的时候，这个弹框隐藏了，visible的值发生了变化。打开窗口也是同理。
        因为关闭窗口这个动作，我们没法用确定的动作去判断这个值，所以用到了vue中的双向绑定原理，在vue中加上.sync统一表示同步的修改了visible的值
        @close：关闭操作时触发
-->
    <el-dialog title="添加用户"  width="50%" :visible.sync="addDialogVisible" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <!-- 权限 -->
        <el-form-item label="权限" prop="urole">
          <el-select v-model="addForm.urole" placeholder="请选择权限">
            <el-option label="普通用户" value="1"></el-option>
            <el-option label="会员用户" value="2"></el-option>
            <el-option label="查询管理员" value="11"></el-option>
            <el-option label="查询+权限控制管理员" value="12"></el-option>
            <el-option label="超级管理员" value="19"></el-option>
          </el-select>
        </el-form-item>
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="uname">
          <el-input v-model="addForm.uname"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item label="密码" prop="upassword">
          <el-input v-model="addForm.upassword" type="password"></el-input>
        </el-form-item>
        <!-- 性别 -->
        <el-form-item label="性别" prop="usex">
          <el-radio-group v-model="addForm.usex">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 手机号 -->
        <el-form-item label="手机号" prop="umobile">
          <el-input v-model="addForm.umobile"></el-input>
        </el-form-item>
        <!-- 家庭住址 -->
        <el-form-item label="家庭住址" prop="uaddress">
          <el-input v-model="addForm.uaddress"></el-input>
        </el-form-item>
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
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :action="uploadAvatarUrl"
            :limit="limit"
            :auto-upload="false"
            ref="avatarUpload"
            :before-upload="beforeAvatarUpload"
            :before-remove="beforeAvatarRemove"
            :on-preview="handleAvatarPreview"
            :on-success="handleAvatarSuccess"
            :on-remove="handleAvatarRemove"
            :on-change="handleAvatarChange"
            :on-exceed="handleAvatarExceed"
            :on-error="handleAvatarError">
            <div class="avatar_border">
              <img v-if="imageUrl" :src="imageUrl" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </div>
          </el-upload>
        </el-form-item>
        <!-- 添加用户按钮 -->
        <el-form-item>
          <el-button type="primary" @click="addUser">添加</el-button>
          <el-button @click="addDialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

<!-- 编辑用户信息对话框 
        :visible指的是属性绑定，表示弹框的显示隐藏，当:visible的值为ture的时候，弹框显示，当为false的时候，弹框隐藏，
        .sync的意思是双向绑定visible的值，当我们关闭窗口的时候，这个弹框隐藏了，visible的值发生了变化。打开窗口也是同理。
        因为关闭窗口这个动作，我们没法用确定的动作去判断这个值，所以用到了vue中的双向绑定原理，在vue中加上.sync统一表示同步的修改了visible的值
-->
    <el-dialog title="编辑用户" width="50%" :visible.sync="editDialogVisible" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <!-- 权限 > 11 -->
        <el-form-item label="权限" prop="urole" v-if="loginUserInfo.urole > 11">
          <el-input v-model="editForm.urole"></el-input> <!-- disabled：只可查看，不可被修改 -->
        </el-form-item>
        <!-- 权限 <= 11 -->
        <el-form-item label="权限" prop="urole" v-if="loginUserInfo.urole <= 11">
          <el-input v-model="editForm.urole" disabled></el-input> <!-- disabled：只可查看，不可被修改 -->
        </el-form-item>
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="uname">
          <el-input v-model="editForm.uname"></el-input> <!-- disabled：只可查看，不可被修改 -->
        </el-form-item>
        <!-- 密码 -->
        <el-form-item label="密码" prop="upassword">
          <el-input v-model="editForm.upassword" type="password"></el-input>
          <!-- <template v-slot="editPwdList">
            <el-input v-model="editForm.upassword" type="password" :disabled="pwdDisabled(editPwdList.row)"></el-input>
          </template> -->
        </el-form-item>
        <!-- 性别 -->
        <el-form-item label="性别" prop="usex">
          <el-radio-group v-model="editForm.usex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 手机号 -->
        <el-form-item label="手机号" prop="umobile">
          <el-input v-model="editForm.umobile"></el-input>
        </el-form-item>
        <!-- 家庭住址 -->
        <el-form-item label="家庭住址" prop="uaddress">
          <el-input v-model="editForm.uaddress"></el-input>
        </el-form-item>

        <!-- 编辑用户按钮 -->
        <el-form-item>
          <el-button type="primary" @click="editUser">添加</el-button>
          <el-button @click="editDialogVisible = false">取消</el-button> <!-- 当点击取消按钮时，触发点击事件editDialogVisible=false，关闭窗口 -->
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name:'userList',

  //在模板渲染成html前调用，即通常初始化某些属性值，然后再渲染成视图。
  created() {
    this.getUserList(); //初始化用户信息默认展示内容
    this.getLoginUserInfo(); //登录用户的信息
  },
  //在模板渲染成html后调用，通常是初始化页面完成后，再对html的dom节点进行一些需要的操作
  mounted(){
    // this.getAvatar();  //获取头像路径
  },
  //只有data里的变量改变并且要在页面重新渲染完成之后,才会进updated生命周期，只改变data里的值但是没有再页面上渲染该值的话并不会触发updated方法。
  updated(){
  },

  data() {
    return {
      //登陆用户的信息
      loginUserInfo:[],

      //查询信息实体
      queryInfo: {
        query: '', //查询信息
        pageNum: 1, //默认当前页
        pageSize: 5, //默认每页最大数
      },
      userList: [],//用户列表    获取所有的用户的信息。如果只想要获取一部分的，就需要在里面添加内容，比如下面的添加用户 --- 表单信息
      total: 0, //总记录数

      // avatarUrl:require("@/assets/userAvatar-image/" + this.getAvatar()),   //用户头像地址拼接

      uploadAvatarUrl:"http://localhost:9000/uploadavatarurl", //上传到后端的全路径地址，默认post请求
      limit:1,
      imageUrl: '',//需要添加的用户头像的二进制类型的地址，例如：blob:http://localhost:8080/202d156a-ff65-4cc4-9419-39c38af1b325

      //添加用户按钮 --- 对话框状态
      addDialogVisible: false,
      //添加用户 --- 表单信息  由于我们不需要填写所有的用户信息(后端填写了urole和ustate)，所以只要把需要填写的信息内容加上就好了
      addForm: {
        urole: "",
        uname: "",
        upassword: "",
        usex: "",
        umobile: "",
        uaddress: "",
      },
      //添加用户基础表单验证。复杂的表单验证在后端填写更佳
      addFormRules: {
        urole: [
          { required: true, message: "请选择权限", trigger: "blur" },
        ],
        uname: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 1, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur",
          },
        ],
        upassword: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 3, max: 18, message: "长度在 3 到 8 个字符", trigger: "blur" },
        ],
        usex: [
          { required: true, message: "请选择性别", trigger: "blur" },
        ],
        umobile: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur",
          },
        ],
        uaddress: [
          { required: true, message: "请输入家庭住址", trigger: "blur" },
          { min: 3, max: 64, message: "长度在 3 到 64 个字符", trigger: "blur",
          },
        ],
      },



      //显示-隐藏编辑用户信息框，默认为隐藏
      editDialogVisible: false,
      //编辑用户信息  由于获取到了全部的信息，所以不需要像添加用户那样写上需要添加的信息
      editForm:{},
      //编辑表单验证。复杂的表单验证在后端填写更佳
      editFormRules: {
        urole: [
          { required: true, message: "请输入权限", trigger: "blur" },
        ],
        uname: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 1, max: 18, message: "长度在 1 到 18 个字符", trigger: "blur",
          },
        ],
        upassword: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur" },
        ],
        usex: [
          { required: true, message: "请输入性别", trigger: "blur" },
        ],
        umobile: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur",
          },
        ],
        uaddress: [
          { required: true, message: "请输入家庭住址", trigger: "blur" },
          { min: 1, max: 64, message: "长度在 1 到 64 个字符", trigger: "blur",
          },
        ],
      },

    };
  },

  methods: {
    //获取用户所有信息以及用户总数 
    async getUserList() {
      // const {data:res} = await this.$http.get("alluser", {params: this.queryInfo});
      //res.userData：用户所有信息
      //res.numbers：用户总数 / 最大列表数
      const res = await this.$http.alluser({...this.queryInfo})
      const { userData, numbers } = res
      this.userList = userData
      this.total = numbers
    },

    //获取登录用户的信息
    getLoginUserInfo(){
      const loginUserInfo = window.localStorage.getItem("loginUserInfo");
      this.loginUserInfo = JSON.parse(loginUserInfo);
    },

    // 获取用户头像
    // async getAvatar() {
    //   console.log("this.userList===>" , this.userList)
    //   const res = await this.$http.useravatar({...this.userList}); 
    //   console.log("res",res);
    //   return res.toString().substring(res.lastIndexOf("/")+1);
    // },


    //每页最大展示数量。默认值为data中queryInfo.pageSize设置的值。
    //newSize：保存了此时每页的最大数
    //this.getUserList()：每次更改都要再执行一次getUserList()方法，否则数据不会发生变化
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.getUserList();
    },

    //当前页。默认值为data中queryInfo.pageNum设置的值，即默认当前页为第一页。
    //newPage：保存了此时处于第几页
    //this.getUserList()：每次更改都要再执行一次getUserList()方法，否则数据不会发生变化
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.getUserList();
    },


    //监听添加用户对话框。 如果对话框关闭，则清除里面已经输入的用户信息
    addDialogClosed(){
      this.$refs.addFormRef.resetFields();
    },
    //添加用户按钮操作。validate进行验证判断，如果验证通过，valid = true
    addUser(){
      this.$refs.addFormRef.validate(async valid => {
        if(!valid) return; //如果 valid = false，说明验证没有通过直接不执行下面的操作
        
        //const {data:res} = await this.$http.post("adduser", this.addForm );
        const res = await this.$http.adduser({...this.addForm});
        
        if(res != "success"){
          return this.$message.error("验证有误，注册失败");
        }

        //如果用户信息添加完成，执行上传头像操作（通过action指向的地址传给相应的后端）
        this.$refs.avatarUpload.submit();
        
        this.$message.success("注册成功");
        this.addDialogVisible = false;       //关闭对话框
        this.$refs.addFormRef.resetFields(); //清除里面已经输入的用户信息
      })
    },


// 添加图片所需要的方法

    //文件状态改变时的钩子。添加文件（回显头像）、上传成功和上传失败时都会被调用
    handleAvatarChange(file,fileList) {
      this.imageUrl = URL.createObjectURL(file.raw);
      console.log("file.raw===>",file.raw)
      console.log("this.imageUrl===>", this.imageUrl);
   },

    //上传文件之前的钩子，进行头像大小和格式的判断
    beforeAvatarUpload(file) {
      const fileName = file.name; //文件名
      const imgSuffix = fileName.substring(fileName.lastIndexOf(".")+1) //获取文件后缀名
      const suffixArray = ['jpg', 'png', 'jpeg', 'gif','bmp']  //限制的文件类型，根据情况自己定义
      if (suffixArray.indexOf(imgSuffix) === -1) {
        return this.$message.error("不支持此格式的文件，请上传图片格式的文件");
      }

      const isLt5M = file.size / 1024 / 1024 < 5;
      if (!isLt5M) {
        return this.$message.error('上传头像图片大小不能超过 5MB!');
      }
    },

    //上传成功时的钩子
    handleAvatarSuccess(response, file, fileList){
      console.log("response", response);
      console.log("file", file);
      console.log("fileList", fileList);
    },

    //删除文件之前的钩子（加上一个功能：若删除文件成功，则把file文件、fileList文件列表和this.imageUrl回显的头像展示都给删除掉）
    //需要进行async await异步处理，等待this.$confirm(`确定移除 ${file.name}？`)的返回值出来，judge才去获取
    //否则，只要执行了删除事件，judge直接就获取到了this.$confirm(`确定移除 ${file.name}？`)的值，而不是获取到它的返回值
    async beforeAvatarRemove(file, fileList) {
      const judge = await this.$confirm(`确定移除 ${file.name}？`);
      if(judge){
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
    handleAvatarError(error,file,fileList){
      this.imageUrl = "";
      console.log("error===>"+error + "---file===>"+file + "---fileList===>"+fileList);
    },




    //删除用户信息
    //uid由此按钮传递参数：<el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteUser(slotProps.row.uid)"></el-button>
    async deleteUser(uid){
      const confirmResult = await this.$confirm("是否删除用户","提示",{ confirmButtonText:"确定", cancelButtonText:"取消", type:"warning"})
      .catch(err => { console.log(err); })
      if (confirmResult != "confirm") return this.$message.info("已取消删除");
      
      // const {data:res} = await this.$http.delete("deleteuser?uid=" + uid);
      const res = await this.$http.deleteuser({ uid })

      if(res != "success") return this.$message.error("删除失败");
      this.$message.success("删除成功");
      this.getUserList();
    },



    //修改用户状态
    // userStateInfo由此滑块标签传递参数：<el-switch v-model="slotProps.row.ustate" @change="userStateChanged(slotProps.row)">
    async userStateChanged(userStateInfo) {
      // 使用put传递参数，也可以使用其他的传参方法
      // const { data: res } = await this.$http.put( `userstate?userId=${userStateInfo.uid}&userState=${userStateInfo.ustate}` );
      //使用api接口封装put请求,无法解决
      const {uid,ustate} = userStateInfo
      const res = await this.$http.userstate( {uid ,ustate} );
      console.log(res);

      if (res != "success") {
        return this.$message.error("操作失败");
      }

      this.$message.success("操作成功");
    },



    //显示编辑用户对话框
    // uid由此按钮传递参数<el-button @click="showEditDialog(slotProps.row.uid)"></el-button>
      async showEditDialog(uid){
        //获取用户编辑信息
        // const {data:res} = await this.$http.get("getupdateuser", {params: {uid} });  //如果是获取形参，就需要这样传值
        const res = await this.$http.getupdateuser({ uid })       
        // console.log(this.$http.getupdateuser( {uid} ));
        
        //把查询到的用户信息反填给editForm编辑表单内供前端显示页面使用
        if(!res) {
          console.log("error,showEditDialog");
          return;
        }
        this.editForm = res;
        //打开对话框
        this.editDialogVisible = true;
      },

      //只有自己才能修改自己的密码，否则变为不可选状态
      // pwdDisabled(editUpassword){
      //   console.log(editUpassword)
      //   if(this.loginUserInfo.upassword == editUpassword){
      //     console.log("1");
      //     return false;
      //   }
      //   console.log("2")
      //   return true;
      // },

    //关闭窗口时清除用户已经输入的信息
    editDialogClosed(){
      this.$refs.editFormRef.resetFields();
    },
    //点击确认按钮来确认修改
    editUser(){
      //validate判断表单验证
      this.$refs.editFormRef.validate(async valid => {
        if(!valid) return;   //如果 valid = false，说明验证没有通过直接不执行下面的操作
        // const {data:res} = await this.$http.post("edituser", this.editForm);  //把用户输入的信息（this.editForm）传入到后端进行校验
        const res = await this.$http.edituser({ ...this.editForm });

        if(res != "success") return this.$message.error("修改失败");

        this.$message.success("修改成功");
        this.editDialogVisible = false;  //修改成功，关闭编辑用户信息框
        this.getUserList(); //执行用户界面，更新修改后的用户信息
      })
    },

  },
};
</script>


<style scoped>

/* 面包屑 */
.breadcrumb{
  margin-bottom:15px;
}

/* 搜索框 */
.search_input{
    margin-bottom:15px;
}

/* 用户列表 */
.userList_el_table{
  text-align: center;
}

/* 用户头像 */

  .avatar_border{
    border: 1px dashed #000000;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar_border:hover {
    border-color: #409EFF;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  /* 添加用户框 - 权限 */
.addDialog_urole{
  margin-bottom:10px;
}

</style>
