<template>
  	<div>
	  	<page-layout>
		    <a-card>
			  	<a-input-search
			      v-model:value="searchValue"
			      @keyup.enter.native="toFilterData"
			      style="margin-bottom: 8px"
			      placeholder="输入关键字按回车搜索"/>
			    <p-table
			      ref="table"
			      :fetch="fetch"
			      :value="obj"
			      :columns="columns"
			      :pagination="pagination"
			      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
			    >
			      <!-- 继承至 a-table 的默认插槽 -->
			      <template #name="{ record }">
			        {{ record.name }}
			      </template>
			    </p-table>
			</a-card>
	    </page-layout>
	    <page-footer></page-footer>
    </div>
    <a-modal
	    v-model:visible="visible"
	    title="添加/修改"
	    :maskClosable="false"
	    :confirm-loading="confirmLoading"
	  >
	    <a-form
	      ref="baseForm"
	      :model="form"
	      :rules="rules"
	      :label-col="{ span: 5 }"
	      :wrapper-col="{ span: 18 }"
	    >
	      ${formInfo}
	    </a-form>
	    <template #footer>
	      <a-button key="back" @click="handleCancel">取消</a-button>
	      <a-button key="submit" type="primary" :loading="loading" @click="onSubmit">提交</a-button>
	    </template>
	</a-modal>
</template>
<script>
import {createVNode, ref} from "vue";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import {message, Modal} from "ant-design-vue";
import {queryDataList, queryData, removeData, saveData} from "/src/api/module/common";

export default {
  data() {
    const table = ref(null);

    const searchValue = ref('');
    
    /// 工具栏
    const toolbar = [
      {
        label: "新增",
        event: this.addRoot
      },
      {
        label: "删除",
        event: this.delDatas
      }
    ];

    /// 字段
    const columns = ${columns};

    /// 数据来源
    const fetch = async (param) => {
	  const that = this;
      return new Promise((resolve) => {
        queryDataList({
          tableName: "${tableName}",
          keyField: "${keyField}",
          dataOrder: "${dataOrder}",
          searchValue: this.searchValue,
          columns: ${searchColumns},//快速查询的列
          pagination: {pageSize: param.pageSize, pageNum: param.pageNum}
        }).then(res => {
		  that.pagination.current = res.data.pageNum;
          resolve({
            total: res.data.total,
            data: res.data.data
          });
        });
      });
    };
	/// 行操作
    const operate = [
      {
        label: "修改",
        event: this.editData
      },
      {
        label: "删除",
        event: this.deleteData
      }
    ];
    const currentId = ref('');
    const visible = ref(false);
    const confirmLoading = ref(false);
    
    const form = ${formColumns};
    
    const rules = {};
    
    const handleCancel = () => {
      visible.value = false;
    };

    return {
      pagination: {current: 1, pageSize: 10}, // 分页配置
      fetch: fetch, // 数据回调
      table,
      toolbar: toolbar, // 工具栏
      columns: columns, // 列配置
      operate: operate, // 行操作
      searchValue,
      currentId,
      visible,
      confirmLoading,
      form,
      rules,
      scroll: {y: 240},
      handleCancel,
      loading: false,
      selectedRowKeys: []
    }
  },
  methods: {
    toFilterData() {
      const table = this.$refs.table;
      table.reload();
    },
    onSubmit() {
      this.$refs.baseForm
        .validate()
        .then(() => {
          this.loading = true;
          saveData({
            tableName: "${tableName}",
            keyField: "${keyField}",
            keyValue: this.form.${keyField},
            data: this.form
          }).then(res => {
            if (res.code === 200) {
              this.form.${keyField} = res.data;
              this.visible = false;
              message.success(res.msg);
              this.$refs.table.reload();
            } else {
              message.error(res.msg);
            }
            this.loading = false;
          });
        })
        .catch(error => {
          this.loading = false;
          console.log("error", error);
        });
    },
    resetForm() {
      this.$refs.baseForm.resetFields();
    },
    reloadForm() {
      this.form = ${formColumns};
      this.form.${keyField} = this.currentId;
      queryData({
        tableName: "${tableName}", keyField: "${keyField}",
        keyValue: this.currentId,
        data: this.form
      }).then(res => {
        if (res.data) {
          this.form = res.data;
        }
        this.visible = true;
      });
    },
    addRoot() {
      this.currentId = '';
      this.reloadForm();
    },
    editData(record) {
      this.currentId = record.${keyField};
      this.reloadForm();
    },
    deleteData(record) {
      const table = this.$refs.table;
      Modal.confirm({
        title: '确认',
        icon: createVNode(ExclamationCircleOutlined),
        content: '确认删除吗？',
        cancelText: '取消',
        okText: '确认',
        onOk() {
          removeData({
            tableName: "${tableName}",
            keyField: "${keyField}",
            keyValue: record.${keyField}
            //级联删除配置
            //,subDataList: [{tableName: '', subField: ''}]
          }).then(res => {
            if (res.code === 200) {
              message.success(res.msg);
              table.reload();
            } else {
              message.error(res.msg);
            }
          });
        }
      });
    },
    delDatas(){
      if (this.selectedRowKeys.length < 1) {
        message.warn("请先勾选需要删除的数据");
        return;
      }
      const table = this.$refs.table;
      const rowids = this.selectedRowKeys.join(',');
      Modal.confirm({
        title: '确认',
        icon: createVNode(ExclamationCircleOutlined),
        content: '确认删除选中的数据吗？',
        cancelText: '取消',
        okText: '确认',
        onOk() {
          removeData({
          	tableName: "${tableName}",
            keyField: "${keyField}",
            keyValue: rowids
            //级联删除配置
            //,subDataList: [{tableName: '', subField: ''}]
          }).then(res => {
            if (res.code === 200) {
              message.success(res.msg);
              table.reload();
            } else {
              message.error(res.msg);
            }
          });
        }
      });
    },
    onSelectChange(selectedRowKeys) {
      //console.log('selectedRowKeys changed: ', selectedRowKeys);
      this.selectedRowKeys = selectedRowKeys;
    }
  }
};
</script>