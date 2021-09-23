<template>
  <div>
    <!-- 面包屑：显示当前页面的路径，快速返回之前的任意页面 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商城活动管理</el-breadcrumb-item>
      <el-breadcrumb-item>活动商品列表</el-breadcrumb-item>
      <el-breadcrumb-item>全部活动商品信息</el-breadcrumb-item>
    </el-breadcrumb>

   <!-- 商品列表主体
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
          <!-- v-model="queryInfo.query"：把商品输入的搜索内容与需要经过模糊查询的商品名进行双向绑定 -->
          <!-- clearable：在input输入框中增加一个删除小按钮； @clear="getGoodActivityList"：通过clear删除事件来触发getGoodActivityList()函数 -->
          <!-- @clear="getGoodActivityList"：当清除input输入框内容时，通过clear删除事件来触发getGoodActivityList()函数 -->
          <!-- @input = "getGoodActivityList"：当输入框发生变化时，触发getGoodActivityList函数 -->
          <!-- @click="getGoodActivityList"：通过click点击事件触发getGoodActivityList()函数，
                       把商品输入的搜索内容通过getGoodActivityList传递给后端进行校验再返回回来 -->
          <el-input placeholder="请输入搜索内容"
            class="search_input"
            v-model="queryInfo.query"
            clearable
            @clear="getGoodActivityList"
            @input = "getGoodActivityList"
          >
            <!-- 搜索/查询按钮 -->
            <el-button slot="append"
              icon="el-icon-search"
              @click="getGoodActivityList"
            ></el-button>
          </el-input>
        </el-col>
      </el-row>

      <!-- 对商品列表进行数据渲染 
              border：增加默认边框样式
              stripe：增加默认隔行换色样式
      -->
      <el-table :data="goodActivityList" border stripe :header-cell-style="{textAlign: 'center'}" :cell-style="{ textAlign: 'center' }" >
        <!-- type="index"：增加索引列
             label="name"：增加索引行信息 -->
        <!-- <el-table-column label="序号" type="index" width="55px"></el-table-column> -->
        <el-table-column label="商品名" prop="gname" width="400px"></el-table-column>
        <el-table-column label="商品图片" prop="gimgurl" width="300px"></el-table-column>
        <el-table-column label="商品价格" prop="gprice" width="100px"></el-table-column>
        <el-table-column label="商品信息" prop="gmessage" width="350px"></el-table-column>
        <!-- 编辑商品/删除商品/角色权限管理 -->
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

                  这里不需要当点击时打开编辑窗口@click=" editDialogVisible=true "，因为后端async showEditDialog()有打开窗口的事件
                  @click=" editDialogVisible=true " 当点击按钮时， editDialogVisible=true显示对话框
                      此判定与编辑商品信息对话框中的:visible.sync="editDialogVisible"进行匹配
                  @keyup.left：鼠标左键松开时触发-->
            <el-tooltip effect="dark" content="删除此活动商品" placement="top-end" :enterable="false" offset="1">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteGoodsActivity(slotProps.row.gname)"></el-button>
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
  </div>
</template>

<script>
export default {
  name: "goodsActivity",

  created() {
      this.getGoodActivityList();
  },
  mounted() {},

  data() {
    return {
        queryInfo:{
            query: "", //查询信息
            pageNum: 1, //默认当前页
            pageSize: 5, //默认每页最大数
        },
        goodActivityList:[], //活动商品信息
        total:0, //活动商品总数量



    };
  },

  methods: {
      //获取商品信息、每页最大数量
    async getGoodActivityList(){
      const res = await this.$http.getGoodActivityList({...this.queryInfo});
      const { goodsActivityCounts,goodsActivities } = res;
      this.goodActivityList = goodsActivities;
      this.total = goodsActivityCounts;
    },

    //删除活动商品
    async deleteGoodsActivity(gname){
        const deleteJudge = await this.$http.deleteGoodsActivity({gname});
        if(deleteJudge == "success"){
            this.getGoodActivityList();
        }else{
            this.$message.error("删除商品失败")
        }
    },

    //每页最大展示数量。默认值为data中queryInfo.pageSize设置的值。
    //newSize：保存了此时每页的最大数
    //this.getGoodList()：每次更改都要再执行一次getGoodList()方法，否则数据不会发生变化
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.getGoodActivityList();
    },

    //当前页。默认值为data中queryInfo.pageNum设置的值，即默认当前页为第一页。
    //newPage：保存了此时处于第几页
    //this.getGoodList()：每次更改都要再执行一次getUserList()方法，否则数据不会发生变化
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.getGoodActivityList();
    },


  },
};
</script>

<style scoped>
/* 面包屑 */
.breadcrumb{
  margin-bottom:15px;
}

/* 搜索输入框 */
.search_input{
    margin-bottom:15px;
}

</style>