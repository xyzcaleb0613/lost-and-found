<template>
  <div>
    <div class="card" style="padding: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <div style="display: flex; margin: 10px 0">
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">公告列表</div>
        <div >
          <el-timeline  reverse slot="reference">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
              <el-popover
                  placement="right"
                  width="200"
                  trigger="hover"
                  :content="item.content">
                <span slot="reference">{{ item.title }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <div style="width: 50%; height: 300px" class="card" id="pie1"></div>
    </div>
    <div style="display: flex; margin: 10px 0;height:300px">
      <div style="width: 50%" class="card" id="pie2"></div>
      <div style="width: 50%" class="card" id="pie3"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

let pieOptions={
  title:{
    text:'',//主标题
    subtext:'',//副标题
    left:'center'
  },
  tooltip:{
    trigger:'item',
    formatter:'{a} <br/>{b} : {c} ({d}%)'
  },
  legend:{
    orient:'vertical',
    left:'left'
  },
  series:[
    {
      name:'',//鼠标移上去显示内容
      type:'pie',
      radius:'50%',
      center:['50%','60%'],
      data:[
        {value:1048,name:'瑞幸咖啡'},//示例数据：name表示维度，value表示对应的值
        {value:735,name:'雀巢咖啡'},
        {value:580,name:'星巴克咖啡'},
        {value:484,name:'栖巢咖啡'},
        {value:300,name:'小武哥咖啡'}
      ]
    }
  ]
}

let pieLostOptions={
  title:{
    text:'',//主标题
    subtext:'',//副标题
    left:'center'
  },
  tooltip:{
    trigger:'item',
    formatter:'{a} <br/>{b} : {c} ({d}%)'
  },
  legend:{
    orient:'vertical',
    left:'left'
  },
  series:[
    {
      name:'',//鼠标移上去显示内容
      type:'pie',
      radius:'50%',
      center:['50%','60%'],
      data:[
        {value:1048,name:'瑞幸咖啡'},//示例数据：name表示维度，value表示对应的值
        {value:735,name:'雀巢咖啡'},
        {value:580,name:'星巴克咖啡'},
        {value:484,name:'栖巢咖啡'},
        {value:300,name:'小武哥咖啡'}
      ]
    }
  ]
}

let pieFoundOptions={
  title:{
    text:'',//主标题
    subtext:'',//副标题
    left:'center'
  },
  tooltip:{
    trigger:'item',
    formatter:'{a} <br/>{b} : {c} ({d}%)'
  },
  legend:{
    orient:'vertical',
    left:'left'
  },
  series:[
    {
      name:'',//鼠标移上去显示内容
      type:'pie',
      radius:'50%',
      center:['50%','60%'],
      data:[
        {value:1048,name:'瑞幸咖啡'},//示例数据：name表示维度，value表示对应的值
        {value:735,name:'雀巢咖啡'},
        {value:580,name:'星巴克咖啡'},
        {value:484,name:'栖巢咖啡'},
        {value:300,name:'小武哥咖啡'}
      ]
    }
  ]
}

export default
{
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: []
    }
  },
  created() {
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
    this.loadAll()
    this.loadLost()
    this.loadFound()
  },
  methods:{
    loadAll(){
      this.$request.get('/echarts/all').then(res=>{
        if(res.code==='200'){
          let chartDom =document.getElementById('pie1');
          let myChart=echarts.init(chartDom);
          pieOptions.title.text=res.data.text
          pieOptions.title.subtext=res.data.subText
          pieOptions.series[0].name=res.data.name
          pieOptions.series[0].data=res.data.data
          myChart.setOption(pieOptions);
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadLost(){
      this.$request.get('/echarts/lost').then(res=>{
        if(res.code==='200'){
          let chartDom =document.getElementById('pie2');
          let myChart=echarts.init(chartDom);
          pieLostOptions.title.text=res.data.text
          pieLostOptions.title.subtext=res.data.subText
          pieLostOptions.series[0].name=res.data.name
          pieLostOptions.series[0].data=res.data.data
          myChart.setOption(pieLostOptions);
        }else{
          this.$message.error(res.msg)
        }
      })

    },
    loadFound(){
      this.$request.get('/echarts/found').then(res=>{
        if(res.code==='200'){
          let chartDom =document.getElementById('pie3');
          let myChart=echarts.init(chartDom);
          pieFoundOptions.title.text=res.data.text
          pieFoundOptions.title.subtext=res.data.subText
          pieFoundOptions.series[0].name=res.data.name
          pieFoundOptions.series[0].data=res.data.data
          myChart.setOption(pieFoundOptions);
        }else{
          this.$message.error(res.msg)
        }
      })

    }
  }
}
</script>
