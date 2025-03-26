<template>

  <div class="main-content">
    <div style="font-size: 17px;margin: 15px ;color: #9a6d2a">
      如有需要，请积极联系<br>
      校园警察： 66668888<br>
      学生事务处：99993333<br>
      我们会为您解决问题。
    </div>
    <div style="width: 60%;margin: 30px auto">
      <div style="font-size: 17px;margin: 20px 0">请对本平台提出建议或者反馈：</div>
      <div>
        <el-input v-model="content" type="textarea" :rows="5" placeholder="请输入你的反馈建议"></el-input>
      </div>
      <div style="text-align: right;margin-top:10px">
        <el-button type="primary" @click="submit">提交</el-button>
      </div>
      <div style="margin: 20px 0;font-size: 17px;color: #9a6d2a;font-weight: 550">
        看看其他小伙伴提出的建议或者反馈（{{commentData.length}}）
      </div>
      <div>
        <el-row v-for="item in commentData" style="margin-top: 30px">
          <el-col :span="4" style="display: flex;align-items: center">
            <img :src="item.avatar" alt="" style="height: 40px; width: 40px;border-radius: 50%">
            <div style="margin-left: 5px">
            {{ item.userName }}
            </div>
          </el-col>
          <el-col :span="16" style="height: 40px;line-height: 40px">{{ item.content }}</el-col>
          <el-col :span="4" style="color: #666666;text-align: right;height: 40px;line-height: 40px">{{ item.time }}</el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  data() {
    return {
      user:JSON.parse(localStorage.getItem('xm-user') || '{}'),
      content:null,
      commentData:[]
    }
  },
  mounted() {
    this.loadComment()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadComment(){
      this.$request.get('/comment/selectAll').then(res=>{
        if(res.code==='200'){
          this.commentData=res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    submit(){
      let data={
        userId:this.user.id,
        content:this.content
      }
      this.$request.post('/comment/add',data).then(res=>{
        if(res.code==='200'){
          this.$message.success('提交成功')
          this.content=null
          this.loadComment()
        }else{
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
