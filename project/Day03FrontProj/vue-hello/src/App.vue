<template>
  <div id="app">
    <!-- <h1>{{ message }}</h1> -->
    <!-- <element-view></element-view> -->
    <el-container style="height: 100%; border: 1px solid #eee">
      <el-header>
        <span>AkeyOdy 职业规划部信息管理</span>
      </el-header>
      <el-container style="height: 80%;">
        <el-aside>
          <el-menu :default-openeds="['1']">
            <el-submenu index="1">
              <template slot="title"><i class="el-icon-message"></i>系统信息管理</template>
              <el-menu-item index="1-1">部门管理</el-menu-item>
              <el-menu-item index="1-2">员工管理</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main width="80%">
          <!-- 1. 单行表单 -->
          <el-form :inline="true" :model="formInline" class="demo-form-inline">
            <el-form-item label="姓名">
              <el-input v-model="formInline.user" placeholder="姓名"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-input v-model="formInline.gender" placeholder="性别">
                <el-option label="男" value="1"></el-option>
                <el-option label="女" value="2"></el-option>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="datepick" type="daterange" align="right" unlink-panels range-separator="至"
                start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">查询</el-button>
            </el-form-item>
          </el-form>
          <!-- 2. 表格 -->
          <el-table :data="tableData">
            <el-table-column prop="name" label="姓名" width="140"></el-table-column>
            <el-table-column label="性别" width="140">
              <template slot-scope="scope">
                {{ scope.row.gender == 1? "男": "女" }}
              </template>
            </el-table-column>
            <el-table-column label="照片" width="120">
              <template slot-scope="scope">
                <img :src="scope.row.image" width="70px">
              </template>
            </el-table-column>
            <el-table-column prop="job" label="工作"></el-table-column>
            <el-table-column prop="entrydate" label="入职日期"></el-table-column>
            <el-table-column prop="updatetime" label="最后编辑时间"></el-table-column>
            <el-table-column label="操作">
              <el-button type="primary" size="mini">编辑</el-button>
              <el-button type="danger" size="mini">删除</el-button>
            </el-table-column>
          </el-table>
          <!-- 3. 翻页 -->
          <el-pagination 
          background
          @size-change="handleSizeChange" 
          @current-change="handleCurrentChange"
          :current-page="currentPage4" 
          :page-sizes="[10, 15, 25, 40]" 
          :page-size="15"
          layout="total, sizes, prev, pager, next, jumper" 
          :total="400">
          </el-pagination>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style>
.el-header {
  background-color: #a4bbdc;
  color: #333;
  text-align: center;
  font-size: 40px;
  height: 20%;
}

.el-aside {
  background-color: rgb(238, 241, 246);
  color: #333;
  width: 20%;
}
</style>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      tableData: [],
      formInline: {
        user: '',
        region: ''
      },
      datepick: ''
    }
  },
  methods: {
    onSubmit() {
      alert("查询成功!")
    },
    handleSizeChange(val) {
      alert(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      alert(`当前页: ${val}`);
    }
  },
  mounted(){
    axios.get("http://yapi.smart-xwork.cn/mock/169327/emp/list").then(res=>{
      this.tableData = res.data.data;
    })
  }
};
</script>