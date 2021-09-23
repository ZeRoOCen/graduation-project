<template>
  <div>
    <!--action="useHttpRequest"       
        :http-request="upload"
        'http://localhost:9000/uploadurl' -->

    <!-- 上传文件组件
          action:'url'        上传文件的后端全路径地址（必填）  :action="uploadUrl"  uploadUrl:"http://localhost:9000/uploadurl"
          accept：            接受的文件类型，这里是接收全部图片类型
          multiple：          支持多选文件
          :limit="limit"：    单次最大允许上传个数
          :file-list="fileList"： 上传的文件列表。格式: [{name: 'food.jpg', url: 'https://xxx.cdn.com/xxx.jpg'}, {name:'', url:''}, ....]
          :auto-upload="false"：  取消自动上传
          ref="fileUpload"：      给fileUpload绑定上此el-upload组件的方法
          before-upload(file)：   上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise(this.$confirm相关) 且被 reject，则停止上传。
          before-remove(file, fileList)： 删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除。
          on-success(file, fileList)：    文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
          on-preview(file)：              点击文件列表中已上传的文件时的钩子
          on-remove(file, fileList)：	    文件列表移除文件时的钩子
          on-exceed(file, fileList)：     文件超出个数限制时的钩子
          on-error(err, file, fileList)： 文件上传失败时的钩子-->
    <el-upload
      :action="uploadUrl"
      accept = "image/*"
      multiple
      :limit="limit"
      :file-list="fileList"
      :auto-upload="false"
      ref="fileUpload"
      :before-upload="beforeUpload"
      :before-remove="beforeRemove"
      :on-success="handleSuccess"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :on-exceed="handleExceed"
      :on-error="handleError"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">
        只能上传图片，且不超过5M
      </div>
    </el-upload>
    <!-- 上传按钮 -->
    <el-button type="primary" @click="imgSubmit">上传<i class="el-icon-upload el-icon--right"></i></el-button>
  </div>
</template>
<script>
export default {
  data() {
    return {
      // uploadUrl:"http://localhost:9000/uploadurl",
      uploadUrl:"http://localhost:9000/uploadavatarurl",
      limit:3,
      fileList: [],
    };
  },
  //'multipart/form-data'
  methods: {
    // 自定义上传
    // async upload(param) {
    //   const formData = new FormData();
    //   const file = param.file;
    //   formData.append('file', file); 
    //   console.log("formData===>", formData.get("file"));
    //   const res = await this.$http.getUploadUrl({...formData},'multipart/form-data');
    // },
    
    //submit()：手动上传文件列表  this.$refs取el-upload组件内的submit方法
    imgSubmit(){
      this.$refs.fileUpload.submit(); 
    },

    //上传文件前的钩子
    beforeUpload(file){
      const isLt5M = file.size / 1024 / 1024 < 5;
      if (!isLt5M) {
        this.$message.error('上传图片大小不能超过 5MB!');
      }
      return isLt5M;
    },
    
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    
    handleSuccess(res,file,fileList){
      console.log("success",res,file,fileList);
    },

    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    
    handlePreview(file) {
      console.log(file);
    },
   
   handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 3 个文件，
        本次选择了 ${files.length} 个文件，
        共选择了 ${files.length + fileList.length}个文件`
      );
    },
    
    handleError(error,file,fileList){
      console.log("error:"+error + "file:"+file + "fileList:"+fileList);
    }
  },
};
</script>

<style scoped>

</style>

