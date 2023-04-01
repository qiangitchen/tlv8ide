<template>
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
</template>
<script>
import {defineComponent, defineExpose, ref} from "vue";
import {queryDataList} from "/src/api/module/common";

export default {
  data() {
    const table = ref(null);

    const searchValue = ref('');

    /// 字段
    const columns = ${columns};

    /// 数据来源
    const fetch = async (param) => {
	  let that = this;
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

    const visible = false;
    const confirmLoading = false;

    return {
      pagination: {current: 1, pageSize: 10}, // 分页配置
      fetch: fetch, // 数据回调
      table,
      columns: columns, // 列配置
      searchValue,
      visible,
      confirmLoading,
      loading: false,
      selectedRowKeys: []
    }
  },
  methods: {
    toFilterData() {
      const table = this.$refs.table;
      table.reload();
    },
    onSelectChange(selectedRowKeys) {
      //console.log('selectedRowKeys changed: ', selectedRowKeys);
      this.selectedRowKeys = selectedRowKeys;
    }
  }
};
</script>