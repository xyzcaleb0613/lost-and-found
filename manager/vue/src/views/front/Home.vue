<template>
  <div class="main-content">
    <div style="width: 80%; margin: 0 auto">
      <el-carousel height="350px">
        <el-carousel-item v-for="item in carouseData" :key="item">
          <img :src="item" alt="" style="width: 100%; height: 350px">
        </el-carousel-item>
      </el-carousel>
      <div style="margin-top:20px">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="card" >
              <div style="display: flex">
                <div style="flex: 1;color: #725b04; font-size: 18px;font-weight: 550 ">最新发布失物</div>
                <div style="flex: 1; text-align: right;color: #666666;cursor:pointer"@click="$router.push('/front/lostSquare')">查看更多</div>
              </div>
              <div style="margin-top: 40px">
                <el-row :gutter="20">
                  <el-col :span="12" v-for="item in lostData" style="margin-bottom:20px" >
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
                        <el-button type="success" @click="contact(item,'失物广场')">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="card" >
              <div style="display: flex">
                <div style="flex: 1;color: #725b04; font-size: 18px;font-weight: 550 ">最新发布招领</div>
                <div style="flex: 1; text-align: right;color: #666666; cursor: pointer" @click="$router.push('/front/foundSquare' )">查看更多</div>
              </div>
              <div style="margin-top: 40px">
                <el-row :gutter="20">
                  <el-col :span="12" v-for="item in foundData" style="margin-bottom:20px" >
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
                        <el-button type="info"  @click="viewContent(item.content)">查看详情</el-button>
                        <el-button type="success"  @click="contact(item,'招领广场')">联系他</el-button>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>
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

import router from "@/router";

export default {

  data() {
    return {
      user:JSON.parse(localStorage.getItem('xm-user')||'{}'),
      carouseData:[
        require('@/assets/imgs/lun-1.jpg'),
        require('@/assets/imgs/lun-2.jpg'),
        require('@/assets/imgs/lun-3.jpg'),
      ],
      lostData:[],
      foundData:[],
      viewVisible:false,
      viewData:null,
      fromVisible:false,
      content:null,
      form:{},
      type:null
    }
  },
  mounted() {
    this.loadLostData()
    this.loadFoundData()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    router() {
      return router
    },
    loadLostData(){
      this.$request.get('/lost/selectNew4').then(res=>{
        if(res.code==='200'){
          this.lostData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadFoundData(){
      this.$request.get('/found/selectNew4').then(res=>{
        if(res.code==='200'){
          this.foundData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    viewContent(content){
      this.viewData=content
      this.viewVisible=true
    },
    contact(item,type){
      this.form=JSON.parse(JSON.stringify(item))
      this.type=type
      this.fromVisible=true
    },
    submit() {
      if(this.user.id===this.form.userId){
        this.$message.warning('您不能联系自己')
        this.content=null
        return
      }
      let data = {
        articleId:this.form.id,
        type:this.type,
        fromId:this.user.id,
        toId:this.form.userId,
        content:this.content
      }
      this.$request.post('/message/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('留言成功，等待对方联系')
          this.content=null
          this.fromVisible=false
          this.type=null
        }else{
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
