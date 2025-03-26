<template>
  <div class="main-content">
    <div style="width:70%; margin: 20px auto">
      <div style="color: #8b4513;font-size:16px;font-weight:550">
        快来看看有没有你丢失的物品吧！如果有，请记得答谢一下帮你找到的小伙伴哦~
      </div>
      <div style="margin-top: 30px">
        <el-input placeholder="请输入物品名称查询" style="width: 200px" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
      <div style="margin-top: 30px">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in lostData" style="margin-bottom:20px" >
            <div class="transition card">
              <div style="display: flex" >
                <img :src="item.img" alt="" style="height: 75px; width: 75px; border: 1px solid #eeeeee; border-radius: 10px ">
                <div style="margin-left: 10px">
                  <div style="font-weight: 550;color: #ef5d26">{{item.name}}</div>
                  <div style="margin-top: 10px;color: #666666">发布人：{{item.userName}}</div>
                  <div style="margin-top: 5px;color: #666666">时间：{{item.time}}</div>
                </div>
              </div>
              <div style="margin-top: 20px; text-align: center">
                <el-button type="info" @click="viewContent(item.content)">查看详情</el-button>
                <el-button type="success" @click="contact(item)">联系他</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
    <el-dialog title="详细信息" :visible.sync="viewVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="viewData" class="w-e-text w-e-text-container"></div>
    </el-dialog>
    <el-dialog title="留言信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px"  >
        <el-form-item prop="title" label="友情提示">
          <span style="color:red">请详细描述你的联系方式，方便对方联系到你</span>
        </el-form-item>
        <el-form-item prop="content" label="留言内容">
          <el-input type="textarea" :rows="5" v-model="content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import E from 'wangeditor'
export default {

  data() {
    return {
      user:JSON.parse(localStorage.getItem('xm-user')||'{}'),
      name: null,
      pageNum: 1,
      pageSize: 8,
      total: 0,
      lostData: [],
      viewVisible: false,
      viewData: null,
      fromVisible: false,
      content: null,
      form: {}
    }
  },
  mounted() {
    this.load(1)


  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/found/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.lostData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load(this.pageNum)
    },
    reset() {
      this.name = null
      this.load(1)
    },
    viewContent(content) {
      this.viewData = content
      this.viewVisible = true
    },
    contact(item) {
      this.form = JSON.parse(JSON.stringify(item))
      this.fromVisible = true

    },
    submit() {
      if(this.user.id===this.form.userId){
        this.$message.warning('您不能联系自己')
        this.content=null
        return
      }
      let data = {
        articleId:this.form.id,
        type:'招领广场',
        fromId:this.user.id,
        toId:this.form.userId,
        content:this.content
      }
      this.$request.post('/message/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('留言成功，等待对方联系')
          this.content=null
          this.fromVisible=false
        }else{
          this.$message.error(res.msg)
        }
      })

    }
  }
}
</script>
