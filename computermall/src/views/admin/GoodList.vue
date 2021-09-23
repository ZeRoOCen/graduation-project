<template>
  <div>
    <!-- 面包屑：显示当前页面的路径，快速返回之前的任意页面 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>信息查询管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品信息列表</el-breadcrumb-item>
      <el-breadcrumb-item>商品信息</el-breadcrumb-item>
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
          <!-- clearable：在input输入框中增加一个删除小按钮； @clear="getGoodList"：通过clear删除事件来触发getGoodList()函数 -->
          <!-- @clear="getGoodList"：当清除input输入框内容时，通过clear删除事件来触发getGoodList()函数 -->
          <!-- @input = "getGoodList"：当输入框发生变化时，触发getGoodList函数 -->
          <!-- @click="getGoodList"：通过click点击事件触发getGoodList()函数，把商品输入的搜索内容通过getGoodList传递给后端进行校验再返回回来 -->
          <el-input placeholder="请输入搜索内容"
            class="search_input"
            v-model="queryInfo.query"
            clearable
            @clear="getGoodList"
            @input = "getGoodList"
          >
            <!-- 搜索/查询按钮 -->
            <el-button slot="append"
              icon="el-icon-search"
              @click="getGoodList"
            ></el-button>
          </el-input>
        </el-col>
        
        <!-- 打开"添加商品"对话框按钮 
                @click=" addDialogVisible=true " 当点击按钮时，addDialogVisible=true显示添加商品对话框
                此判定与编辑商品信息对话框中的:visible.sync="addDialogVisible"进行匹配-->
        <el-col :span="4">
          <el-button type="primary" @click=" addDialogVisible=true ">添加商品</el-button>
        </el-col>
      </el-row>

<!-- 对商品列表进行数据渲染 
              border：增加默认边框样式
              stripe：增加默认隔行换色样式
      -->
      <el-table :data="goodList" border stripe :header-cell-style="{textAlign: 'center'}" :cell-style="{ textAlign: 'center' }" >
        <!-- type="index"：增加索引列
             label="name"：增加索引行信息 -->
        <!-- <el-table-column label="序号" type="index" width="55px"></el-table-column> -->
        <el-table-column label="商品id" prop="gid" width="70px"></el-table-column>
        <el-table-column label="商品名" prop="gname" width="150px"></el-table-column>
        <el-table-column label="商品价格" prop="gprice" width="80px"></el-table-column>
        <el-table-column label="主分类" prop="mgsort" width="70px"></el-table-column>
        <el-table-column label="子分类" prop="sgsort" width="70px"></el-table-column>
        <!--商品状态滑块判定  
              :active-value="1"：激活状态。后台传递过来的state是int类型
              :inactive-value="0"：关闭状态。同上
              v-slot：作用域插槽；父组件可以调用子组件的数据；v-slot只能用在template上
              @change：当输入框失焦的时候触发. @change="goodStateChanged(scope.row)" -->
        <el-table-column label="商品状态" prop="gstate" width="80px">
          <template v-slot="slotProps">
            <el-switch v-model="slotProps.row.gstate"
              @change="goodStateChanged(slotProps.row)"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
            >
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column label="商品地址" prop="gimgurl" width="120px"></el-table-column>
        <el-table-column label="商品描述" prop="gmessage" width="400px"></el-table-column>
        
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
            <el-tooltip effect="dark" content="编辑商品" placement="top-end" :enterable="false" offset="1">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(slotProps.row.gid)"></el-button>
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip effect="dark" content="删除商品" placement="top" :enterable="false">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteGood(slotProps.row.gid)"></el-button>
            </el-tooltip>
            <!-- 添加商品至活动商品按钮 -->
            <el-tooltip effect="dark" content="添加至活动" placement="top-start" :enterable="false" offset="-1">
              <el-button type="warning" icon="el-icon-setting" size="mini" @click="addGoodsToActivity(slotProps.row.gid)"></el-button>
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
    


    <!-- 添加商品对话框 
        :visible指的是属性绑定，表示弹框的显示隐藏，当:visible的值为ture的时候，弹框显示，当为false的时候，弹框隐藏，
        .sync的意思是双向绑定visible的值，当我们关闭窗口的时候，这个弹框隐藏了，visible的值发生了变化。打开窗口也是同理。
        因为关闭窗口这个动作，我们没法用确定的动作去判断这个值，所以用到了vue中的双向绑定原理，在vue中加上.sync统一表示同步的修改了visible的值
        @close：关闭操作时触发
-->
    <el-dialog title="添加商品"  width="50%" :visible.sync="addDialogVisible" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <!-- 商品名 -->
        <el-form-item label="商品名" prop="gname">
          <el-input v-model="addForm.gname"></el-input>
        </el-form-item>
        <!-- 商品价格 -->
        <el-form-item label="价格" prop="gprice">
          <el-input v-model="addForm.gprice"></el-input>
        </el-form-item>
        <!-- 主分类 -->
        <el-form-item label="主分类" prop="mgsort">
          <el-radio-group v-model="addForm.mgsort" @change="mgsortSelect">
            <el-radio label="整机">整机</el-radio>
            <el-radio label="配件">配件</el-radio>
            <el-radio label="外设">外设</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 子分类 -->
        <el-form-item label="子分类" prop="sgsort">
          <el-radio-group v-model="addForm.sgsort">
            <el-radio label="一体机" :disabled="sgsortWholeMachine">一体机</el-radio>
            <el-radio label="台式机" :disabled="sgsortWholeMachine">台式机</el-radio>
            <el-radio label="平板电脑" :disabled="sgsortWholeMachine">平板电脑</el-radio>
            <el-radio label="游戏本" :disabled="sgsortWholeMachine">游戏本</el-radio>
            <el-radio label="笔记本" :disabled="sgsortWholeMachine">笔记本</el-radio>
            <el-radio label="轻薄本" :disabled="sgsortWholeMachine">轻薄本</el-radio>

            <el-radio label="CPU" :disabled="sgsortFitting">CPU</el-radio>
            <el-radio label="SSD" :disabled="sgsortFitting">SSD</el-radio>
            <el-radio label="显卡" :disabled="sgsortFitting">显卡</el-radio>
            <el-radio label="显示器" :disabled="sgsortFitting">显示器</el-radio>
            <el-radio label="机箱外壳" :disabled="sgsortFitting">机箱外壳</el-radio>
            <el-radio label="组装机箱" :disabled="sgsortFitting">组装机箱</el-radio>

            <el-radio label="U盘" :disabled="sgsortPeripheral">U盘</el-radio>
            <el-radio label="摄像头" :disabled="sgsortPeripheral">摄像头</el-radio>
            <el-radio label="移动硬盘" :disabled="sgsortPeripheral">移动硬盘</el-radio>
            <el-radio label="键盘" :disabled="sgsortPeripheral">键盘</el-radio>
            <el-radio label="鼠标" :disabled="sgsortPeripheral">鼠标</el-radio>
            <el-radio label="鼠标垫" :disabled="sgsortPeripheral">鼠标垫</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 上传图片 
              el-upload自带两个参数，file文件和filelist文件列表
              drag：启用拖拽上传
              action：必选参数，上传的地址；String类型
              multiple：支持多选文件
              :file-list="imgList"：上传的文件列表存放在数组中, 例如: [{name: 'food.jpg', url: 'https://xxx.cdn.com/xxx.jpg'}]
              show-file-list：显示已上传文件列表
              list-type="text"  ：文件列表的类型（text文本文字 / picture图片 / picture-card图片-卡片）	
              accept：支持的文件类型
              :limit="imgLimit"：设置单次最大上传个数
              :before-upload="beforeUpload"：设置上传前的钩子函数（比如限制上传的每张图片的大小）
              :on-preview="handlePreview"：点击文件列表中已上传的文件时的钩子
              :on-remove="handleRemove"：文件列表移除文件时的钩子
              :before-remove="beforeRemove"：删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除。
              :on-exceed="handleExceed"：文件超出个数限制时的钩子-->
        <!-- <el-form-item label="商品图片">
          <el-upload
            class=""
            drag
            action="http://localhost:9000/goodImg"
            multiple
            :file-list="imgList"
            show-file-list
            list-type="text"
            accept = ".jpg, .jpeg, .png, .gif, .bmp, .JPG, .JPEG, .PBG, .GIF, .BMP"
            :limit="imgLimit"
            :before-upload="beforeUpload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-exceed="handleExceed">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item> -->

        <!-- type="textarea"：多行文本 
             :rows="3" ：默认文本域高度为3-->
        <el-form-item label="商品描述" prop="gmessage">
          <el-input v-model="addForm.gmessage" type="textarea" :rows="3"></el-input>
        </el-form-item>
        
        <!-- 添加商品按钮 -->
        <el-form-item>
          <el-button type="primary" @click="addGood">添加</el-button>
          <el-button @click="addDialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 编辑商品信息对话框 
        :visible指的是属性绑定，表示弹框的显示隐藏，当:visible的值为ture的时候，弹框显示，当为false的时候，弹框隐藏，
        .sync的意思是双向绑定visible的值，当我们关闭窗口的时候，这个弹框隐藏了，visible的值发生了变化。打开窗口也是同理。
        因为关闭窗口这个动作，我们没法用确定的动作去判断这个值，所以用到了vue中的双向绑定原理，在vue中加上.sync统一表示同步的修改了visible的值
-->
    <el-dialog title="编辑商品" width="50%" :visible.sync="editDialogVisible" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <!-- 商品名 -->
        <el-form-item label="商品名" prop="gname">
          <el-input v-model="editForm.gname"></el-input> <!-- disabled：只可查看，不可被修改 -->
        </el-form-item>
        <!-- 密码 -->
        <el-form-item label="价格" prop="gprice">
          <el-input v-model="editForm.gprice"></el-input>
        </el-form-item>
        <!-- 商品主分类 -->
        <el-form-item label="主分类" prop="mgsort">
          <el-radio-group v-model="editForm.mgsort">
            <el-radio label="整机">整机</el-radio>
            <el-radio label="配件">配件</el-radio>
            <el-radio label="外设">外设</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 商品子分类 -->
        <el-form-item label="子分类" prop="sgsort">
          <el-radio-group v-model="editForm.sgsort">
            <el-radio label="一体机">一体机</el-radio>
            <el-radio label="台式机">台式机</el-radio>
            <el-radio label="平板电脑">平板电脑</el-radio>
            <el-radio label="游戏本">游戏本</el-radio>
            <el-radio label="笔记本">笔记本</el-radio>
            <el-radio label="轻薄本">轻薄本</el-radio>
            <el-radio label="CPU">CPU</el-radio>
            <el-radio label="SSD">SSD</el-radio>
            <el-radio label="显卡">显卡</el-radio>
            <el-radio label="显示器">显示器</el-radio>
            <el-radio label="机箱外壳">机箱外壳</el-radio>
            <el-radio label="U盘">U盘</el-radio>
            <el-radio label="摄像头">摄像头</el-radio>
            <el-radio label="移动硬盘">移动硬盘</el-radio>
            <el-radio label="键盘">键盘</el-radio>
            <el-radio label="鼠标">鼠标</el-radio>
            <el-radio label="鼠标垫">鼠标垫</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 商品图片修改 -->
        <!-- <el-form-item label="" prop="">
          <el-input v-model="editForm"></el-input>
        </el-form-item> -->
        <!-- 商品描述 -->
        <el-form-item label="商品描述" prop="gmessage">
          <el-input v-model="editForm.gmessage"></el-input>
        </el-form-item>
        <!-- 编辑商品按钮 -->
        <el-form-item>
          <el-button type="primary" @click="editGood">修改</el-button>
          <el-button @click="editDialogVisible = false">取消</el-button> <!-- 当点击取消按钮时，触发点击事件editDialogVisible=false，关闭窗口 -->
        </el-form-item>
      </el-form>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name:'goodList',

  created(){
    this.getGoodList();
  },

  data(){
    return{
      queryInfo: {
        query: "", //查询信息
        pageNum: 1, //默认当前页
        pageSize: 5, //默认每页最大数
      },

      goodId:"",

      goodList:[], //商品信息
      total:0, //商品总数量

      // imgList: [],//图片列表
      // imgLimit:5 //图片上传限制为五张

      //添加商品按钮 --- 对话框状态
      addDialogVisible: false,
      //添加商品 --- 表单信息  由于我们不需要填写所有的商品信息(后端填写了urole和ustate)，所以只要把需要填写的信息内容加上就好了
      addForm: {
        gname: "",
        gprice: "",
        sgsort: "",
        mgsort: "",
        gmessage: "",
      },

      //添加商品子分类区域
      sgsortWholeMachine: true,
      sgsortFitting: true,
      sgsortPeripheral: true,

      //添加商品基础表单验证。复杂的表单验证在后端填写更佳
      addFormRules: {
        gname: [
          { required: true, message: "请输入商品名", trigger: "blur" },
          { min: 3, max: 128, message: "长度在 3 到 128 个字符", trigger: "blur",
          },
        ],
        gprice: [
          { required: true, message: "请输入价格", trigger: "blur" },
          { min: 1, max: 12, message: "长度在 1 到 12 个字符", trigger: "blur" },
        ],
        sgsort: [
          { required: true, message: "请选择商品子分类", trigger: "blur" },
          { min: 1, max: 18, message: "请选择商品子分类", trigger: "blur" },
        ],
        mgsort: [
          { required: true, message: "请选择商品主分类", trigger: "blur" },
          { min: 1, max: 18, message: "请选择商品主分类", trigger: "blur",
          },
        ],
        gmessage: [
          { required: true, message: "请输入商品详细内容", trigger: "blur" },
          { min: 3, max: 255, message: "长度在 3 到 255 个字符", trigger: "blur",
          },
        ],
      },

      //显示/隐藏编辑商品信息框，默认为隐藏
      editDialogVisible: false,
      //编辑商品信息  由于获取到了全部的信息，所以不需要像添加商品那样写上需要添加的信息
      editForm:{},
      //编辑表单验证。复杂的表单验证在后端填写更佳
      editFormRules: {
        gname: [
          { required: true, message: "请输入商品名", trigger: "blur" },
          { min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur",
          },
        ],
        gprice: [
          { required: true, message: "请输入商品价格", trigger: "blur" },
          { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
        ],
        mgsort: [
          { required: true, message: "请选择商品主分类", trigger: "blur" },
          { min: 1, max: 8, message: "请选择商品主分类", trigger: "blur" },
        ],
        sgsort: [
          { required: true, message: "请选择商品子分类", trigger: "blur" },
          { min: 1, max: 8, message: "请选择商品子分类", trigger: "blur",
          },
        ],
        gmessage: [
          { required: true, message: "请输入商品描述", trigger: "blur" },
          { min: 3, max: 255, message: "长度在 3 到 255 个字符", trigger: "blur",
          },
        ],
      },

    }
  },

  methods:{

    //获取商品信息、每页最大数量
    async getGoodList(){
      const res = await this.$http.getGoodList({...this.queryInfo});
      const { goodCounts,goods } = res;
      this.goodList = goods;
      this.total = goodCounts;
    },

    //每页最大展示数量。默认值为data中queryInfo.pageSize设置的值。
    //newSize：保存了此时每页的最大数
    //this.getGoodList()：每次更改都要再执行一次getGoodList()方法，否则数据不会发生变化
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.getGoodList();
    },

    //当前页。默认值为data中queryInfo.pageNum设置的值，即默认当前页为第一页。
    //newPage：保存了此时处于第几页
    //this.getGoodList()：每次更改都要再执行一次getUserList()方法，否则数据不会发生变化
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.getGoodList();
    },

    //主分类选择时，打开对应的子分类为可选状态
    mgsortSelect(){
      if(this.addForm.mgsort == "整机"){
        this.sgsortWholeMachine = false;
        this.sgsortFitting = true;
        this.sgsortPeripheral = true;
      }else if(this.addForm.mgsort == "配件"){
        this.sgsortWholeMachine = true;
        this.sgsortFitting = false;
        this.sgsortPeripheral = true;
      }else{
        this.sgsortWholeMachine = true;
        this.sgsortFitting = true;
        this.sgsortPeripheral = false;
      }
    },

    //监听添加对话框。如果对话框关闭，则清除里面已经输入的商品信息并让所有子分类变为不可选状态
    addDialogClosed(){
      this.$refs.addFormRef.resetFields();

      this.sgsortWholeMachine = true;
      this.sgsortFitting = true;
      this.sgsortPeripheral = true;
    },

    //添加商品按钮操作。validate进行验证判断，如果验证通过，valid = true
    addGood(){
      this.$refs.addFormRef.validate(async valid => {
        if(!valid) return; //如果 valid = false，说明验证没有通过直接不执行下面的操作
        
        const res = await this.$http.addGood({ ...this.addForm })
        
        // const {data:res} = await this.$http.post("addgood", this.addForm );
        if(res != "success"){
          return this.$message.error("验证有误，注册失败");
        }
        this.$message.success("注册成功");
        this.addDialogVisible = false;       //关闭对话框
        this.$refs.addFormRef.resetFields(); //清除里面已经输入的商品信息
      })
    },

    //删除用户信息
    //uid由此按钮传递参数：<el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteUser(slotProps.row.uid)"></el-button>
    async deleteGood(gid){
      const confirmResult = await this.$confirm("是否删除商品","提示",{ confirmButtonText:"确定", cancelButtonText:"取消", type:"warning"})
      .catch(err => { console.log(err); })
      if (confirmResult != "confirm") return this.$message.info("已取消删除");
      
      // const {data:res} = await this.$http.delete("deleteuser?uid=" + uid);
      const res = await this.$http.deleteGood({ gid }) //根据gid匹配后端商品进行删除操作

      if(res != "success") return this.$message.error("删除失败");
      this.$message.success("删除成功");
      this.getGoodList();
    },

     //修改商品状态
    // goodStateInfo由此滑块标签传递参数：<el-switch v-model="slotProps.row.ustate" @change="goodStateChanged(slotProps.row)">
    async goodStateChanged(goodStateInfo) {
      // 使用put传递参数，也可以使用其他的传参方法
      // const { data: res } = await this.$http.put( `goodstate?goodId=${goodStateInfo.uid}&goodState=${goodStateInfo.ustate}` );
      //使用api接口封装put请求,无法解决，所以限制改成post请求
      const {gid,gstate} = goodStateInfo
      const res = await this.$http.goodStateChanged( {gid ,gstate} );

      if (res != "success") {
        return this.$message.error("操作失败");
      }
      this.$message.success("操作成功");
    },

      // 显示编辑商品对话框
      // uid由此按钮传递参数<el-button @click="showEditDialog(slotProps.row.gid)"></el-button>
      async showEditDialog(gid){
        //获取商品编辑信息
        // const {data:res} = await this.$http.get("getupdategood", {params: {gid} });  //无封装api接口的get请求。如果是获取形参，就需要这样传值
        this.goodId = gid.toString();
        const res = await this.$http.getUpdateGood({ ...this.goodId })       
        //把查询到的商品信息反填给editForm编辑表单内供前端显示页面使用
        if(!res) {
          console.log("error,showEditDialog");
          return;
        }
        this.editForm = res;
        //打开对话框
        this.editDialogVisible = true;
      },

    //关闭窗口时清除商品已经输入的信息
    editDialogClosed(){
      this.$refs.editFormRef.resetFields();
    },
    //点击确认按钮来确认修改
    editGood(){
      //validate判断表单验证
      this.$refs.editFormRef.validate(async valid => {
        if(!valid) return;   //如果 valid = false，说明验证没有通过直接不执行下面的操作
        // const {data:res} = await this.$http.post("editgood", this.editForm);  //把商品输入的信息（this.editForm）传入到后端进行校验
        const res = await this.$http.editGood({ ...this.editForm })

        if(res != "success") return this.$message.error("修改失败");
        this.$message.success("修改成功");
        this.editDialogVisible = false;  //修改成功，关闭编辑商品信息框
        this.getGoodList(); //执行商品界面，更新修改后的商品信息
      })
    },

    //根据商品ID将商品添加至活动商品表中
    async addGoodsToActivity(gid){
      const resActivityJudge = await this.$http.addGoodsToActivity({gid});
      if(resActivityJudge == "success"){
        this.$message.success("商品添加至活动成功");
      }else{
        this.$message.error("商品已存在活动中....");
      }
    },



    // //上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。参数含有一个file：function(file)
    // beforeUpload(file){
    //   console.log(file);
    //   const isLt5M = file.size / 1024 / 1024 < 5;
    //   if (!isLt5M) {
    //     this.$message.error('上传图片大小不能超过 5MB!');
    //   }
    //   return isLt5M;
    // },
    // //文件列表移除文件时的钩子。function(file, fileList)
    // handleRemove(file, fileList) {
    //   console.log(file, fileList);
    // },
    // //点击文件列表中已上传的文件时的钩子	function(file)
    // handlePreview(file) {
    //   console.log(file);
    // },
    // //文件超出个数限制时的钩子	function(files, fileList)
    // handleExceed(files, fileList) {
    //   this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    // },
    // //删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除。function(file, fileList)
    // beforeRemove(file, fileList) {
    // return this.$confirm(`确定移除 ${ file.name }？`);
    // },


  }
}
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