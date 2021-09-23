<template>
    <!-- 登录容器  :style="bgImage" 加入背景图片的方法之一-->
    <div class="login_container" :style="bgImage">
        <!-- 登录块 -->
        <div class="login_box">
            <!-- 头像 -->
            <div class="avatar_box">
                <img src="../assets/dinosaur-logo.jpg" class="avatar" alt="嗷呜~~~">
            </div>
            <!-- 表单区域 
            :rules：绑定验证功能  
            ref：绑定$refs的名字，用于使用此标签上的方法
            :model：model为表单数据对象，:model="dataName"为表单数据对象与data中的数据进行绑定
            prop： 填写内容为表单域 model 中的字段(无需打点使用，如此题的loginForm.uname)；
                   在使用 validate、resetFields 方法的情况下，使用此属性，此时该属性是必填的。
            v-model="dataName" ：双向绑定data中的某属性的值
            -->
            <el-form class="form_box" :rules="loginRules" ref="loginFormRef" :model="loginForm" label-width="0">
                <!-- 用户名 prop属性设置为需校验的字段名，此题对应data中loginRules内的属性名。在使用validate和resetFields方法时，该属性是必需的
                            autofocus：html5功能 ===> 在页面加载时，域自动地获得此焦点-->
                <el-form-item class="form-userBox" prop="uname" label="用户名" label-width="100px">
                    <el-input class="form-uname" v-model="loginForm.uname" prefix-icon="iconfont icon-denglu" placeholder="用户名" autofocus></el-input>
                </el-form-item>
                <!-- 密码 -->
                <el-form-item class="form-pwdBox" prop="upassword" label="密码" label-width="100px">
                    <el-input class="form-upassword" v-model="loginForm.upassword" type="password" prefix-icon="iconfont icon-mima" placeholder="密码"></el-input>
                </el-form-item>
                 <!-- 提交/重置表单  @click的方法名带不带小括号都可以，带了表明有实参要传入到方法体内
                        @keyup:enter="loginBtn"  这里还能加上一个键盘事件绑定到登录按钮上，不过需要先解决全局键盘事件问题（即无焦点也能触发）-->
                <el-form-item class="form-button">
                    <el-button class="form-submitButton" @click="loginBtn" type="primary">登录</el-button>
                    <el-button class="form-resetButton" @click="resetLoginForm()" type="info">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
    //为什么data要return：不使用return包裹的数据会在项目的全局可见，会造成变量污染；使用return包裹后数据中变量只在当前组件中生效，不会影响其他组件
    data(){
        return{
            //！属性名要与数据库中user表中的字段名对应才能成功传值
            loginForm:{
                uname:'',
                upassword:''
            },
            loginRules:{
                uname:[
                    //required是否为必填项；
                    //message当内容不符合规范(必填项没信息或者长度不对)时展示的信息内容；
                    //trigger:'blur / change' 失去焦点时触发 / 数据改变时触发
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 1, max: 12, message: '长度在 1 到 12 个字符', trigger: 'blur' }
                ],
                upassword:[
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' }
                ]
            },
            //加入背景图片以及修改它的样式
            bgImage:{
                backgroundImage: "url("+require("../assets/login-image/background-image.jpg")+")",
                backgroundSize: "80% 100%",
                backgroundRepeat: "no-repeat",
                backgroundPosition: "50% -330px"
            }

        }
    },

    methods:{
        //resetFields（Form-item Methods方法）：对该表单项进行重置，将其值重置为初始值并移除校验结果（重置对象为el-form里面的全部子el-form-item）
        resetLoginForm(){
            this.$refs.loginFormRef.resetFields();
        },
        //validate（此处使用Form Events事件，还有Form Methods方法的使用）：任一表单项被校验后触发。  
        //          参数为回调函数（名字任意，此题为valid）：被校验的表单项 prop 值，校验是否全部通过，全部通过为true，反之为false
        loginBtn(){
            this.$refs.loginFormRef.validate( async valid => {
                if(!valid) return this.$message.error("用户名或密码错误");
                    //this.$http.post第一个参数：验证成功，使用axios通过post请求发送给/login页面，/login对应的是后端的LoginController
                    //this.$http.post第二个参数：携带参数传递给后端LoginController中。this.loginForm携带的是上面data中return的loginForm中的键值对
                    //data:res：后端return回来的值(此题为LoginController中的res_json)保存在data(别名res)中（data可以取别名，此题别名为res）
                    //res.allUser：可以通过res.打点的方法来取出res中的键值对。比如res.allUser(后端即res_json.allUser)
                //const {data:res} = await this.$http.post("login", this.loginForm); //未使用API接口时的post请求方式

                const res = await this.$http.login( {...this.loginForm} ); //封装请求方式的API接口

                //localStorage和sessionStorage的区别：localStorage在浏览器关闭后还可以保存数据，而sessionStorage在关闭窗口或标签页之后将会删除这些数据。https://www.runoob.com/jsref/prop-win-sessionstorage.html
                //sessionStorage("key","value") 和 localStorage("key","value") 属性允许在浏览器中存储 key/value 对的数据。
                //数据在google控制台中的Application--Local Storage中显示，firefox在控制台中的DOM -- localStorage
                if(res.loginUserInfo != null){
                    //取出用户信息中的用户名保存在session中，在后端ChatEndpoint.java中使用
                    const userName = res.loginUserInfo.uname;
                    window.sessionStorage.setItem("userName",userName);
                    //localStorage存储的是String字符串类型，所以需要使用JSON.stringif()转换成JSONString字符串类型
                    const loginUserInfo = JSON.stringify(res.loginUserInfo);
                    //获取到的res.loginUserInfo信息保存在loginUserInfo名字中，为之后的router/index.js中的getItem使用
                    window.localStorage.setItem("loginUserInfo",loginUserInfo);
                    // 不需要这样做，可以直接在需要离线的地方先让uonline=0变为离线状态，再赋值给loginUserInfo.setItem("uonline",loginUserInfo.uonline)
                        // window.localStorage.removeItem("uonline"); //删除local中的uonline状态
                        // window.sessionStorage.setItem("uonline",loginUserInfo.uonline);//把uonline存储在sessionStorage中，当页面关闭时，用户处于离线状态
            //switch 判断用户信息和用户角色权限：
                    switch(res.loginUserInfo.urole){
                        case 1:
                            this.$message.success("普通用户登录成功");
                            this.$router.push({path:"/userhome"});
                            break;
                        case 2:
                            this.$message.success("会员用户登录成功");
                            this.$router.push({path:"/userhome"});
                            break;
                        case 11:
                            this.$message.success("查询管理员登录成功");
                            this.$router.push({path:"/adminhome"});
                            break;
                        case 12:
                            this.$message.success("查询/权限控制管理员登录成功");
                            this.$router.push({path:"/adminhome"});
                            break;
                        case 13:
                            this.$message.success("商品客服登录成功");
                            this.$router.push({path:"/spare_customer_view"});
                            break;
                        case 19:    
                            this.$message.success("超级管理员登录成功");
                            this.$router.push({path:"/adminhome"});
                            break;
                        default:
                            this.$message.success("未知权限登录，请重新登录");
                            this.$router.push({path:"/login"});
                            break;
                    }
                }else{
                    this.$message.error("用户名或密码错误，请重新输入");
                    this.$router.push({path:"/login"});
                }

            //if 判断用户信息和用户角色权限：已经用上面的switch判断代替
                // if( res.loginUserInfo != null && res.loginUserInfo.urole>=10){
                //     window.localStorage.setItem("loginUserInfo",res.loginUserInfo); //获取到的res.allUser信息保存在allUser名字中，为之后的router/index.js中的getItem使用
                //     this.$message.success("管理员登录成功");
                //     this.$router.push({path:"/adminhome"}); //跳转到后台首页
                // }else if(res.loginUserInfo != null && res.loginUserInfo.urole<10){
                //     window.localStorage.setItem("loginUserInfo",res.loginUserInfo); //获取到的res.allUser信息保存在allUser名字中，为之后的router/index.js中的getItem使用
                //     this.$message.success("用户登录成功");
                //     this.$router.push({path:"/userhome"}); //跳转到后台首页
                // }
                // else{
                //     this.$message.error("登录失败");
                //     this.$router.push({path:"/login"});
                // }

            })
        },
    }
}
</script>


<style scoped>
    .login_container{
        width:100%;
        height:100%;
    }
    .login_box{
        width:600px;
        height:400px;
        margin:auto;
        text-align: center;
        padding-top: 150px;
    }
    .avatar{
        height:120px;
        width:120px;
        border-radius:50% 50% 50% 50%;
        overflow: hidden;
    }
    .form-userBox{
        margin-left:10px;
    }
    .form-uname{
        width:350px;
        margin-left:-80px;
    }
    .form-pwdBox{
        margin-left:10px;
    }
    .form-upassword{
        width:350px;
        margin-left:-80px;
    }
    .form-submitButton{
        width:100px;
    }
    .form-resetButton{
        width:100px;
    }
</style>
