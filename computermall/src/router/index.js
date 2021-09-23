import Vue from 'vue'
import VueRouter from 'vue-router'

//登录页面
import Login from '../views/Login.vue'

//后台页面
import AdminHome from '../views/admin/Home.vue'
import WelcomeAdminHome from '../views/admin/WelcomeHome.vue'
import UserList from '../views/admin/UserList.vue'
import GoodList from '../views/admin/GoodList.vue'
import uploadTest from '../views/admin/uploadTest.vue'
import GoodsActivity from '../views/admin/GoodsActivity.vue'
import SpareCustomerView from '../views/admin/SpareCustomerView.vue'

//前台页面
import UserHome from '../views/user/UserHome.vue'
import PersonView from '../views/user/PersonView.vue'
import GoodInterface from '../views/user/GoodInterface.vue'
import ContactCustomerService from '../components/user/ContactCustomerService.vue'
import SpareContactCustomer from '../views/user/SpareContactCustomer.vue'

Vue.use(VueRouter)

const routes = [
  {
    path:'/',
    component:Login,
  },
  //登录页面
  {
    path:'/login',
    component:Login
  },
  //上传测试页面
  {
    path:'/uploadtest',
    component:uploadTest
  },

  //后台页面
  {
    path:'/adminhome',//主展示页面<router-view>存放在APP.vue中
    name:'home',
    component:AdminHome,
    redirect:'/welcomeadminhome', //子页面默认展示页面<router-view>存放在Home.vue中（即Home页面中<el-main>展示的内容）
    children:[
      {
        path:'/welcomeadminhome',
        name:'welcomeHome',
        component:WelcomeAdminHome
      },
      {
        path:'/user_List',
        name:'userList',
        component:UserList //用户信息列表页面组件
      },
      {
        path:'/good_list',
        name:'goodList',
        component:GoodList //商品信息列表页面组件
      },
      {
        path:'/goods_activity',
        name:'goodsActivity',
        component:GoodsActivity //商品信息列表页面组件
      },
    ]
  },
  {
    path:'/spare_customer_view',
    name:'spareCustomerView',
    component:SpareCustomerView //备用后台客服界面
  },

  //前台页面
  {
    path:'/userhome',
    name:'userHome',
    component: UserHome
  },
  {
    path:'/personview',
    name:'personView',
    component: PersonView
  },
  {
    path:'/contact_customer_service',
    name:'contactCustomerService',
    component: ContactCustomerService
  },
  {
    path:'/good_interface',
    name:'goodInterface',
    component: GoodInterface
  },
  {
    path:'/spare_contact_customer',
    name:'spareContactCustomer',
    component: SpareContactCustomer
  },
  {
    path:'/spare_contact_customer',
    name:'spareContactCustomer',
    component: SpareContactCustomer
  },

]

//创建 router 实例，然后传 `routes` 配置
const router = new VueRouter({
  routes
})

//全局挂载路由导航守卫（前端完成路由跳转限制监控工作）
  //to：将要访问
  //from：从哪访问
  //next：接着做   next()：继续访问to路径   next("url")：重定向到url上
router.beforeEach((to,from,next)=>{
  //从Login.vue的window.localStorage.setItem("loginUserInfo",res.loginUserInfo)中通过key名("loginUserInfo")
  //来获取值（值是对象存储的，需要通过某种方式才能将对象转换成JSON字符串，此处不需要）
  const userFlag = window.localStorage.getItem("loginUserInfo");

  if(to.path == "/login") return next(); //如果直接访问login页面，放行
  if(to.path != "/login" && !userFlag){ //如果用户没有登录(即没有用户信息userFlag)就跳转到除了login页面之外的页面，就禁止进入其他页面，直接跳转到login页面
    Vue.prototype.$message.error("请先登录您的账户"); //这里无法使用this.$message。因为this没有指向vue，无法使用。所以要使用全局变量Vue.prototype
    return next("/login");
  }
  next();//放行（前提：没有直接访问login页面 + userFlag不为空）
  document.documentElement.scrollTop = 0 //进入页面后，定位到顶部
})

export default router
