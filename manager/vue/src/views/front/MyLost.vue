<template>
  <div class="main-content">
    <div style="width: 70%; margin: 0 auto">
      <div style="margin: 20px 0">
        <el-button type="primary" plain @click="handleAdd">发布失物</el-button>
      </div>
      <div class="table">
        <el-table :data="tableData" stripe  >
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="img" label="物品主图" >
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image style="width: 40px; height: 40px" v-if="scope.row.img"
                          :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="物品名称" show-overflow-tooltip></el-table-column>
          <el-table-column prop="content" label="物品描述">
            <template v-slot="scope">
             <el-button type="primary" size="mini" @click="viewEditor(scope.row.content)">点击查看</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>
          <el-table-column prop="time" label="时间"></el-table-column>

          <el-table-column label="操作" width="180" align="center">
            <template v-slot="scope">
              <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
              <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination" style="margin-top: 20px">
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


      <el-dialog title="信息" :visible.sync="fromVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" :model="form" >
          <el-form-item prop="img" label="物品图片">
              <el-upload
                  class="avatar-uploader"
                  :action="$baseUrl + '/files/upload'"
                  :headers="{ token: user.token }"
                  list-type="picture"
                  :on-success="handleImgSuccess"
              >
                <el-button type="primary">上传图片</el-button>
              </el-upload>
            </el-form-item>
          <el-form-item prop="name" label="物品名称">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="物品状态">
            <el-select v-model="form.status" placeholder="请选择状态" style="width:100%">
              <el-option label="丢失中" value="丢失中"></el-option>
              <el-option label="已找到" value="已找到"></el-option>
            </el-select>
            <el-input v-model="form.status" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="content" label="物品描述">
            <div id="editor"></div>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="详细信息" :visible.sync="viewVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
        <div v-html="viewData" class="w-e-text w-e-text-container"></div>
      </el-dialog>


    </div>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {

  data() {
    return {
      editor: null,
      user:JSON.parse(localStorage.getItem('xm-user') || '{}'),
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      fromVisible: false,
      viewVisible: false,
      form: {},
      viewData:null,
    }
  },
  mounted() {
    this.load(1)

  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = '请输入内容'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(content)
        })
      })
    },
    viewEditor(content){
      this.viewData = content
      this.viewVisible = true
    },
    load(pageNum){
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/lost/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId:this.user.id
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })

    },
    handleAdd(){
      this.form={}
      this.initWangEditor('')
      this.fromVisible=true

    },
    handleEdit(row){
      this.form=JSON.parse(JSON.stringify(row))
      this.initWangEditor(this.form.content || '')
      this.fromVisible=true
    },
    del(id){
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/lost/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    save(){
      this.form.content=this.editor.txt.html()
      this.$request({
      url: this.form.id ? '/lost/update' : '/lost/add',
      method: this.form.id ? 'PUT' : 'POST',
      data: this.form
    }).then(res => {
      if (res.code === '200') {  // 表示成功保存
        this.$message.success('保存成功')
        this.fromVisible = false
      } else {
        this.$message.error(res.msg)  // 弹出错误的信息
      }
    })

    },
    handleCurrentChange(pageNum){
      this.pageNum=pageNum
      this.load(this.pageNum)
    },
    handleImgSuccess(res){
      this.form.img=res.data
    }
  }
}
</script>
